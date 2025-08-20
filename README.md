# WeatherInsight

AI 기반 날씨 추천 및 분석 플랫폼 - 위치 기반 날씨 조회, AI 추천을 제공하는 통합 웹 서비스

## 🚀 주요 기능

- **Geocoder API 연동**: 주소 기반 좌표 변환
- **OpenWeather API 연동**: 현재 날씨 및 기상 정보 수집
- **Gemini API 연동**: 수집된 날씨 데이터를 기반으로 AI 추천/분석
- **데이터 저장/조회**: WeatherInfo(날씨) & WeatherRecommendation(추천) 관리
- **로그 관리**: Interceptor + GlobalExceptionHandler 기반 로그 기록 및 에러 처리

## 🏗️ 아키텍처

```
┌─────────────────┐       ┌────────────────────────┐       ┌────────────────────┐
│   Web Client    │◀─────▶│    Weather Insight    │◀─────▶│   External APIs   │
│  (Web Browser)  │       │   (Spring Boot App)    │       │ ────────────────── │
└─────────────────┘       └────────────────────────┘       │ - Geocoder API     │
         ▲                ┌────────────────────────┐       │ - OpenWeather API  │
         │                │ - Controller           │       │ - Gemini API       │
         │                │ - Service Layer        │       └────────────────────┘
         │                │ - Repository (JPA)     │
         │                │ - Logging / Error      │
         │                └────────────────────────┘
         ▼                ┌────────────────────────┐
┌──────────────────┐◀─────│ - WeatherInfo          │
│     Database     │◀─────│ - Recommendation       │
│     (MySQL)      │      └────────────────────────┘
└──────────────────┘
```

## 📋 기술 스택

- Java 17 + Spring Boot 3.x
- Spring RestClient
- MySQL
- Gemini API (Google AI)
- Geocoder API
- OpenWeather API

## 🚀 빠른 시작

### 1. 저장소 클론
```bash
git clone <repository-url>
cd weather-insight
```
### 2. 실행
```bash
cd weather-insight
./mvnw spring-boot:run
```
### 3. 접속

- `URL: http://localhost:8080`
- `Swagger 문서: http://localhost:8080/swagger-ui.html`

## 🔌 API 엔드포인트

### 개별 API

#### 날씨 정보

- `GET /api/v1/weather` - 주소 입력 → 좌표 변환 → 날씨 조회
- `POST /api/v1/weather` - 날씨 정보 저장
- `GET /api/v1/weather/{id}` - 특정 날씨 정보 조회
- `POST /api/v1/weather/{id}` - 날씨 정보 조회 -> Gemini 추천 생성

#### 추천/분석

- `POST /api/v1/analysis` - Gemini 추천/분석 저장
- `GET /api/v1/analysis/{id}` - 특정 추천 조회

## 🔧 설정

```properties
# Gemini API
gemini.api.key=your_gemini_key_here
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent

# Geocoder API
geocoder.api.key=your_geocoder_key_here
geocoder.api.url=https://api.vworld.kr/req/address

# OpenWeather API
open-weather.api.key=your_open_weather_key_here
open-weather.api.url=https://api.openweathermap.org/data/2.5/weather
```

## 📊 응답 형식

### 성공 응답
```json
{
  "success": true,
  "message": "응답 메세지",
  "data": "응답 본문",
  "timestamp": "2025-08-18T17:12:31.969007",
  "errorCode": null
}
```

### 에러 응답
```json
{
  "success": false,
  "message": "응답 메세지",
  "timestamp": "2025-08-18T17:12:31.969007",
  "errorCode": "예외 클래스"
}
```

## 🚨 에러 처리

### 주요 에러 타입

- `GeocoderApiException`: 주소 → 좌표 변환 실패
- `OpenWeatherApiException`: 날씨 API 호출 조회 실패
- `GeminiApiException`: Gemini AI 추천 생성 실패
- `NotFoundException`: 데이터 조회 실패

### 에러 처리 방식

- `LoggingInterceptor` → 모든 요청/응답 로그 기록
- `GlobalExceptionHandler` → 예외 발생 시 통합 처리

## 📚 상세 문서

[**Weather Insight API 문서**](docs/api.md)  
[**기술적 의사 결정 과정 문서**](docs/decision-making.md)

## 🛠️ 개발

### 프로젝트 구조
```
weather-insight/
├── src/main/java/com/example/weatherinsight
│   ├── client/                    # 외부 API Client
│   ├── config/                    # 설정
│   ├── controller/                # REST API 컨트롤러
│   ├── dto/                       # 데이터 전송 객체
│   ├── entity/                    # 엔티티
│   ├── interceptor/               # 인터셉터
│   ├── repository/                # 레포지토리
│   ├── exception/                 # 예외 처리
│   ├── service/                   # 비즈니스 로직
│   └── WeatherInsightApplication  # 메인 클래스
├── src/main/resources/
│   ├── application.properties     # 설정 파일
└── build.gradle
```

## 🤝 기여

1. Fork the Project
2. Create your Feature Branch (git checkout -b feature/AmazingFeature)
3. Commit your Changes (git commit -m 'Add some AmazingFeature')
4. Push to the Branch (git push origin feature/AmazingFeature)
5. Open a Pull Request

## 📄 라이선스

## 📞 지원

프로젝트에 대한 문의사항이 있으시면 이슈를 생성해 주세요.

