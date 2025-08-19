# WeatherInsight API 문서

## 개요
WeatherInsight API는 **주소 기반 날씨 조회**와 **AI 추천/분석**을 제공하는 Spring Boot 기반 웹 서비스입니다.  
사용자는 주소를 입력해 날씨를 조회할 수 있고, AI 분석을 통해 맞춤형 추천을 받을 수 있습니다.


## 기본 정보
- **Base URL**: http://localhost:8080/api/v1
- **인증**: 필요 없음 (개인 프로젝트용)
- **데이터 저장**: JPA 기반 RDBMS (WeatherInfo, Analysis, Logs)


## 엔드포인트

### 1. 날씨 정보

#### (1) 날씨 조회
`GET /api/v1/weather`  
주소 입력을 받아 좌표 변환 후, OpenWeather API에서 날씨를 조회합니다.

**요청 예시**

`GET /api/v1/weather?address=서울`

**응답 예시**
```json
{
"success": true,
"message": "날씨 정보 조회에 성공했습니다",
"data": {
"id": null,
"address": "서울",
"description": "맑음",
"temperature": 31.48,
"feelsLike": 36.45,
"latitude": 37.566700969,
"longitude": 126.97834678
},
"timestamp": "2025-08-18T17:12:31.969007",
"errorCode": null
}
```
#### (2) 날씨 정보 저장

`POST /api/v1/weather`

조회된 날씨 정보를 DB에 저장합니다.

**요청 예시**

```json
{
  "address": "서울",
  "description": "맑음",
  "temperature": 31.48,
  "feelsLike": 36.45,
  "latitude": 37.566700969,
  "longitude": 126.97834678
}
```
**응답 예시**

```json
{
  "success": true,
  "message": "날씨 정보 저장에 성공했습니다",
  "data": {
    "id": 1,
    "address": "서울",
    "description": "맑음",
    "temperature": 31.48,
    "feelsLike": 36.45,
    "latitude": 37.566700969,
    "longitude": 126.97834678
  },
  "timestamp": "2025-08-18T08:16:25.760Z",
  "errorCode": null
}
```

#### (3) 특정 날씨 정보 조회

`GET /api/v1/weather/{id}`

**요청 예시**
```
GET /api/v1/weather/1
```

**응답 예시**

```json
{
"success": true,
"message": "날씨 정보 조회에 성공했습니다",
"data": {
"id": 1,
"address": "서울",
"description": "약간의 구름이 낀 하늘",
"temperature": 28.35,
"feelsLike": 32.64,
"latitude": 37.566700969,
"longitude": 126.97834678
},
"timestamp": "2025-08-18T17:24:00.287289",
"errorCode": null
}
```

#### (4) 날씨 기반 추천 생성
`POST /api/v1/weather/{id}`

저장된 날씨 정보를 기반으로 Gemini API를 호출해 추천/분석을 생성합니다.

**요청 예시**

```
POST /api/v1/weather/1
```
**응답 예시**

```json
{
  "success": true,
  "message": "날씨 분석/추천 생성에 성공했습니다",
  "data": {
    "address": "서울",
    "coordinates": {
      "lat": 37.566700969,
      "lon": 126.97834678
    },
    "weatherData": {
      "temperature": 28.35,
      "feelsLike": 32.64,
      "description": "약간의 구름이 낀 하늘"
    },
    "analysis": "안녕하세요! 서울의 날씨 비서입니다. (중략)..."
  },
  "timestamp": "2025-08-18T17:27:07.7941185",
  "errorCode": null
}
```

### 2. 추천/분석

#### (1) Gemini 추천/분석 저장

`POST /api/v1/analysis`

Gemini API로부터 받은 추천/분석을 저장합니다.

**요청 예시**

```json
{
  "address":  "서울",
  "temperature": 32.64,
  "feelsLike": 28.35,
  "description": "구름조금",
  "textContent": "안녕하세요! 친절한 날씨 해설가이자 비서입니다. (중략)..."
}
```

**응답 예시**

```json
{
  "success": true,
  "message": "날씨 분석/추천이 성공적으로 저장되었습니다",
  "data": {
    "address": "서울",
    "coordinates": {
      "lat": 37.566700969,
      "lon": 126.97834678
    },
    "weatherData": {
      "temperature": 28.35,
      "feelsLike": 32.64,
      "description": "약간의 구름이 낀 하늘"
    },
    "analysis": "안녕하세요! 친절한 날씨 해설가이자 비서입니다. (중략)..."
  },
  "timestamp": null,
  "errorCode": null
}
```
#### (2) 특정 추천 조회
`GET /api/v1/analysis/{id}`

**요청 예시**
`GET /api/v1/analysis/1`

**응답 예시**

```json
{
  "success": true,
  "message": "저장된 추천 조회에 성공했습니다.",
  "data": {
    "id": 1,
    "cityName": "서울",
    "weatherDataJson": {
      "feelsLike": "32.64",
      "description": "약간의 구름이 낀 하늘",
      "temperature": "28.35"
    },
    "textContent": "안녕하세요! 친절한 날씨 해설가이자 비서입니다. (중략)...",
    "createdAt": "2025-08-15T14:42:27.768+00:00"
  },
  "timestamp": "2025-08-18T17:40:13.3711816",
  "errorCode": null
}
```
## 에러 처리

### 에러 예시

잘못된 주소 입력

```json
{
  "success": false,
  "message": "날씨 정보 조회에 실패했습니다",
  "data": null,
  "timestamp": "2025-08-19T14:43:30.182509",
  "errorCode": "com.example.weatherinsight.exception.custom.GeocoderApiException"
}
```
날씨 API 호출 실패
```json
{
  "success": false,
  "message": "날씨 정보 조회에 실패했습니다",
  "data": null,
  "timestamp": "2025-08-19T14:43:30.182509",
  "errorCode": "com.example.weatherinsight.exception.custom.OpenWeatherApiException"
}
```
Gemini 추천 실패
```json
{
  "success": false,
  "message": "날씨 분석/추천 생성에 실패했습니다",
  "data": null,
  "timestamp": "2025-08-19T14:43:30.182509",
  "errorCode": "com.example.weatherinsight.exception.custom.GeminiApiException"
}
```

## 사용 예시

### CURL 예시

날씨 조회
```bash
curl -X GET "http://localhost:8080/api/v1/weather?address=서울"
```
날씨 정보 저장
```bash
curl -X POST "http://localhost:8080/api/v1/weather" \
  -H "Content-Type: application/json" \
  -d '{
      "address": "서울",
      "description": "맑음",
      "temperature": 31.48,
      "feelsLike": 36.45,
      "latitude": 37.566700969,
      "longitude": 126.97834678
    }'
```
날씨 기반 추천 생성
```bash
curl -X POST "http://localhost:8080/api/v1/weather/1"
```
추천 저장
```bash
curl -X POST "http://localhost:8080/api/v1/analysis" \
  -H "Content-Type: application/json" \
  -d '{
        "address":  “서울",
        "temperature": 32.64,
        "feelsLike": 28.35,
        "description": "구름조금",
        "textContent": “안녕하세요! 친절한 날씨 해설가이자 비서입니다. (중략)..."
}'
```

추천 조회
```bash
curl -X GET "http://localhost:8080/api/v1/analysis/1"
```

### 주의사항

Geocoder API, OpenWeather API, Gemini API 키가 application.properties 또는 .env에 반드시 설정되어야 합니다.

모든 응답은 JSON 형식으로 반환됩니다.

에러 발생 시 success: false와 함께 error와 error message가 포함됩니다.

Gemini API 호출은 무료 키 사용 시 일일 제한이 있습니다.