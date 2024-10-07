# 상품 관리 시스템

이 프로젝트는 **상품과 브랜드 관리**를 위한 **백엔드 애플리케이션**입니다. 관리자는 브랜드 및 상품을 추가, 수정, 삭제할 수 있으며, 고객은 브랜드와 카테고리별 최저가 상품 정보를 조회할 수 있습니다. **Spring Boot**, **JPA**, **H2 데이터베이스**를 사용하여 데이터 관리를 처리하며, **REST API** 형태로 제공됩니다.

## 주요 기능

### 관리자 기능

1. **브랜드 및 상품 관리**:
    - 관리자는 새로운 브랜드를 생성하고, 상품을 추가, 수정, 삭제할 수 있습니다.
    - 엔드포인트: `/v1/admin/brand-item-management` (POST)

### 고객 기능

1. **카테고리별 최저가 조회**:
    - 각 카테고리별 최저가 상품과 해당 브랜드 정보를 조회할 수 있습니다.
    - 엔드포인트: `/v1/items/lowest-prices` (GET)

2. **단일 브랜드 최저가 조회**:
    - 단일 브랜드로 전체 카테고리 상품을 구매할 경우 최저가 상품과 총액을 조회할 수 있습니다.
    - 엔드포인트: `/v1/items/lowest-price-brand` (GET)

3. **카테고리별 최저/최고가 브랜드 조회**:
    - 특정 카테고리에서 최저가와 최고가 브랜드를 조회할 수 있습니다.
    - 엔드포인트: `/v1/items/categories/{categoryName}/prices` (GET)

## 사용 기술

- **Java 17**
- **Spring Boot 3.3.4**
- **JPA/Hibernate**
- **H2 Database** 
- **Spock Framework** (테스트 프레임워크, Groovy 사용)
- **Swagger API 문서**
- **Spring Security**

## 프로젝트 설정

### 실행 방법

프로젝트 빌드 및 실행:

```bash
./gradlew clean build
./gradlew bootRun
```

애플리케이션은 `http://localhost:8080`에서 실행됩니다.

### H2 콘솔

H2 데이터베이스 콘솔은 다음 주소에서 사용할 수 있습니다:  
`http://localhost:8080/h2-console`  
다음 정보를 입력하세요:

- **JDBC URL**: `jdbc:h2:mem:homework`
- **Username**: `sa`
- **Password**: *(비워두세요)*

### Swagger 문서

Swagger UI는 다음 주소에서 사용할 수 있습니다:  
`http://localhost:8080/swagger-ui/index.html`


### application.yml 

```yaml
spring:
  h2:
    console:
      enabled: true
  datasource:
    url: jdbc:h2:mem:homework
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        format_sql: true
```

## 폴더 구조

```bash
src/
├── main/
│   ├── java/
│   │   └── com/musinsa/homework/
│   │       ├── common/             # 공통 유틸리티 및 설정
│   │       │   ├── config/         # 보안 및 환경 설정 관련 클래스
│   │       │   ├── exception/      # 예외 처리
│   │       │   └── util/           # 공통 유틸리티
│   │       ├── controller/         # 컨트롤러
│   │       │   ├── api/            # REST API 컨트롤러
│   │       │   │   ├── request/    # REST API 요청 DTO
│   │       │   │   └── response/   # REST API 응답 DTO
│   │       │   └── web/            # 웹 UI 컨트롤러
│   │       ├── entity/             # JPA 엔티티 (Brand, Item, Category)
│   │       ├── repository/         # 데이터 접근 계층 (Repository)
│   │       ├── service/            # 비즈니스 로직 계층
│   │       └── ApplicationRunner   # 애플리케이션 실행 관련 클래스 (데이터 로딩)
│   └── resources/
│       ├── static/                 # 정적 자원 (CSS, JS 등)
│       ├── templates/              # 타임리프 템플릿
│       └── application.yml         # 프로젝트 설정 파일
└── test/
    └── groovy                      # Groovy 기반 테스트 코드
        └── com/musinsa/homework/
            ├── common/         
            ├── controller/       
            ├── repository/        
            └── service/           
```

## API 기능 가이드

애플리케이션의 메인 페이지에서 **상품 관리**와 관련된 주요 API 기능을 브라우저를 통해 직접 사용할 수 있습니다. 화면에서 제공되는 버튼을 클릭하여 각 기능을 쉽게 테스트할 수 있으며, 관리자는 상품과 브랜드를
추가, 수정, 삭제할 수 있고, 고객은 상품 정보를 조회할 수 있습니다.

### 메인 페이지 URL

```
http://localhost:8080/
```

### API 기능 설명

- **API1: 카테고리별 최저가격 조회**
    - **기능 설명**: 각 카테고리별로 최저가 상품을 조회할 수 있습니다.
    - **사용 방법**: "카테고리 별 최저가격" 버튼을 클릭하면, 카테고리별로 최저가 상품과 해당 브랜드를 조회할 수 있는 화면으로 이동합니다.
    - **페이지경로**: `http://localhost:8080/page/items/lowest-prices`

- **API2: 단일 브랜드 최저가격 조회**
    - **기능 설명**: 단일 브랜드로 모든 카테고리 상품을 구매할 경우 최저가를 제공하는 브랜드 정보를 조회할 수 있습니다.
    - **사용 방법**: "단일 브랜드 최저가격" 버튼을 클릭하면, 단일 브랜드로 최저가 상품 목록과 총액을 조회할 수 있는 화면으로 이동합니다.
    - **페이지경로**: `http://localhost:8080/page/items/lowest-price-brand` (GET)

- **API3: 카테고리별 최저/최고 가격 브랜드 조회**
    - **기능 설명**: 특정 카테고리 내에서 최저가와 최고가를 기록한 브랜드를 조회할 수 있습니다.
    - **사용 방법**: "카테고리별 최저/최고 가격 브랜드" 버튼을 클릭하면, 특정 카테고리에서 최고가와 최저가를 기록한 브랜드를 조회할 수 있는 화면으로 이동합니다.
    - **페이지경로**: `http://localhost:8080/page/items/categories/prices (GET)

- **API4: 상품 리스트 조회 및 관리**
    - **기능 설명**: 현재 저장된 모든 상품의 목록을 조회할 수 있으며, 각 상품의 브랜드, 카테고리, 가격 정보 등을 확인할 수 있습니다. 상품을 추가, 수정, 삭제할 수 있는 관리 기능도 포함되어
      있습니다.
    - **사용 방법**: "상품 리스트" 버튼을 클릭하면 상품 목록 화면으로 이동하며, 추가/수정/삭제 작업을 수행할 수 있는 버튼이 함께 표시됩니다.
    - **페이지경로**: `http://localhost:8080/page/items` (GET)

## 테스트

### 테스트 실행

이 프로젝트는 **Spock Framework**를 사용하여 테스트를 작성했습니다. 모든 테스트를 실행하려면 다음 명령어를 사용하세요:

```bash
./gradlew test
```

---

