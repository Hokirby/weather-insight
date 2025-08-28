# 🛠️ 기술적 의사 결정 과정
본 프로젝트를 개발하며 고려했던 주요 기술적 결정들을 정리했습니다. 각 결정은 프로젝트의 목표와 효율성을 고려하여 신중하게 이루어졌습니다.

## 1. 사용자 인증 방식: 로그인 vs. 익명 사용자

### 배경 및 고민
포트폴리오 프로젝트에서 Spring Security와 로그인(회원 관리) 기능을 구현할지 여부를 고민했습니다. 사용자 관리 기능을 추가하면 '실제 서비스'처럼 보이고, Security 경험을 어필할 수 있다는 장점이 있지만, 프로젝트의 핵심 기능인 날씨 정보 조회와 추천 기능 개발에 집중하기 위해 과감히 생략하기로 결정했습니다.

### 결정
현재는 별도의 사용자 로그인 기능을 구현하지 않았습니다. 대신, 모든 사용자가 서비스를 이용할 수 있도록 모든 요청을 허용했습니다. 이는 보안에 대한 고려를 완전히 배제한 것이 아니라, 사용자 인증만 생략했다는 것을 보여주기 위함입니다. 추후 서비스 확장 시 익명 로그인(AnonymousAuthentication) 또는 정식 회원 로그인 기능을 추가하여 확장성을 확보할 계획입니다.

## 2. API 통신 방식: RestClient의 exchange()와 onStatus() 활용

### 배경 및 고민

외부 API와의 통신을 위해 Spring 6부터 지원하는 RestClient를 도입했습니다.
초기에는 retrieve()와 exchange() 중 어느 메서드를 사용할지 고민했지만, 실제로는 API별 특성에 따라 다른 방식을 선택했습니다.

### 결정

#### Geocoder API
+ 주소 → 좌표 변환 과정에서 예외 케이스가 매우 다양했습니다. 
+ 예: 잘못된 주소 입력, 응답 구조 불일치, 상태 코드 에러, 좌표 없음 등. 
+ 따라서 retrieve() 기반으로 onStatus()를 적극 활용하여 상태 코드별 예외 처리를 세분화했습니다. 
+ 이후 추가적으로 응답 본문 null 체크 / 상태 값 검증 / 좌표 존재 여부 검증 등을 단계별로 수행했습니다.

#### Gemini API / OpenWeather API
+ 응답 구조는 비교적 단순하지만, 에러 발생 시 상세한 로깅이 필요했습니다. 
+ 그래서 응답 전체(상태 코드·헤더·바디)를 직접 다룰 수 있는 **exchange()** 를 사용했습니다. 
+ 이를 통해 실패 응답의 원문을 그대로 로깅하여 디버깅 효율을 높였습니다.

코드 예시
📍 Geocoder API – retrieve() + onStatus() 기반
```java
GeocoderResponse geocoderResponse = restClient.get()
        .uri(uri)
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(HttpStatusCode::isError, (request, response) -> {
            String body = new String(response.getBody().readAllBytes());
            log.error("[Geocoder] Raw Response: {}", body);

            throw new GeocoderApiException("Geocoder API Request Failed: " + body, response.getStatusCode());
        })
        .body(GeocoderResponse.class);

// 이후 Null 체크 + 상태 코드 검증 + 좌표 존재 여부 확인
```
📍 Gemini API - exchange() 기반
```java
return restClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(geminiRequest)
                .exchange((request, response) -> {
                    if (response.getStatusCode().isError()) {
                        String errorBody = "";
                        try {
                            errorBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                        } catch (IOException e) {
                            log.error("[Gemini] Error Body Read Failed: ", e);
                        }
                        log.error("[Gemini] Request Failed ({}): {}", response.getStatusCode(), errorBody);
                        throw new GeminiApiException("Gemini Request Failed: " + errorBody, response.getStatusCode());
                    }
                    GeminiResponse geminiResponse = response.bodyTo(GeminiResponse.class);
                    log.info("[Gemini] Response: {}", geminiResponse);

                    String generatedText = extractText(geminiResponse);
                    log.info("[Gemini] Text Extracted: {}", generatedText);
                    return generatedText;
                });
```

## 3. 데이터베이스 저장 방식: JSON 타입 컬럼 활용

### 배경 및 고민 
OpenWeather API로부터 받은 날씨 정보를 데이터베이스에 저장할 때, 복잡한 JSON 형태의 데이터를 어떻게 효율적으로 관리할지 고민했습니다. 일반적으로 JSON 데이터를 String 타입으로 저장할 수도 있지만, 이 경우 직렬화/역직렬화 로직을 직접 구현해야 하는 번거로움이 있습니다.

### 결정
MySQL은 TEXT 타입 컬럼을 JSON 형태로 활용할 수 있습니다. 이를 위해 Spring Data JPA와 Hybernate-types 라이브러리를 사용하고 @JdbcTypeCode(SqlTypes.JSON) 어노테이션을 적용하여, 객체 필드를 JSON 문자열로 자동 변환하여 저장하고, 조회 시 자동으로 객체로 복원되도록 구현했습니다.

```Java
@Entity
public class WeatherRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> weatherDataJson; // JSON 저장
}
```
장점: 직렬화/역직렬화 로직을 JPA가 자동으로 처리하여 개발 편의성이 높아집니다.

단점: MySQL의 TEXT 타입은 JSON 전용 인덱스를 지원하지 않아 JSON 내부 필드를 기준으로 한 검색 성능이 제한될 수 있습니다. 본 프로젝트에서는 해당 기능이 필요하지 않아 이 방식을 채택했습니다.