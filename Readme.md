# ClothShoppingMall (의류 커머스 최저가 검색 엔진 백엔드 API)

Spring Boot 4.0.6 환경과 Querydsl을 기반으로 구축한 고성능 의류 카테고리별 최저가/최고가 분석 및 검색 플랫폼 엔진입니다. 단순한 데이터 CRUD를 넘어, 대용량 트래픽 상황에서의 대형 조회 쿼리 병목을 개선하기 위해 전용 통계 장부 레이어를 운용하고 효율적인 인프라 모니터링 파이프라인을 구축했습니다.

---

## Tech Stack (기술 스택)

### Backend Engine & Framework
- **Language**: Java 17
- **Framework**: Spring Boot 4.0.6
- **Build Tool**: Gradle
- **Database ORM**: Spring Data JPA & Querydsl JPA 5.0.0 (Jakarta 호환 코어)

### Infra & Monitoring & Test Tools
- **RDB Environment**: H2 Database (MySQL Mode 데이터 정합성 호환)
- **DevOps**: Docker, Docker-compose (Multi-Container 가상화 인프라)
- **Monitoring**: Spring Boot Actuator, Prometheus, Grafana Dashboard
- **Performance Test**: k6 Open Model (Constant Arrival Rate) & Stage Load Testing

---

## Core Architecture & Optimization Points (핵심 설계 및 성능 최적화)

### 1. 조회 성능 극대화를 위한 '통계 장부 엔티티' 분리 및 캐싱
- **문제 인식**: 수천만 건의 실시간 상품 데이터(products)를 가진 이커머스 도메인에서 사용자가 카테고리별 최저가를 요청할 때마다 데이터베이스에 Aggregate(MIN(), GROUP BY) 집계 연산을 때리면 디스크 I/O 병목으로 인해 전체 인프라가 다운될 위험이 있습니다.
- **해결 방안**: 무거운 조회 책임을 물리적으로 분리하기 위해 카테고리별 최저가 브랜드와 가격 정보만을 전담 마크하는 **BrandCategoryLowestPriceEntity** 테이블을 별도로 운용(CQRS 아키텍처 변형 형태)합니다.
- **데이터 라이프사이클 및 정합성 제어**:
  - **Context 구동 초기화**: DataInitializer 컴포넌트를 구성하여 애플리케이션 런타임 업(Up) 시점에 시스템에 내장된 기초 데이터셋 명세(schema.sql, data.sql)와 장부 테이블 간의 동기화를 자동으로 수행합니다.
  - **실시간 데이터 동기화**: 관리자 레이어에서 신규 상품이 추가되거나(addProduct), 기존 최저가 기준을 흔드는 상품 변동 및 삭제(deleteProduct)가 감지되면, 비즈니스 로직단에서 실시간으로 스코어를 추적하여 장부 데이터를 원자적(Atomic)으로 업데이트합니다.
  - **로컬 캐시 레이어 튜닝**: 실시간 최저가 API 진입점에 @Cacheable("lowestPriceEachCategory") 엔진을 연동하여, 동일 바운더리의 반복 조회 요청에 대해 자원 소모 없이 메모리에서 즉시 0ms 응답을 리턴하도록 구현했습니다.

### 2. 우선순위 체인 기반의 분산 정렬 알고리즘 탑재
- 카테고리별 최저가 브랜드를 그룹핑하고 연산하는 과정에서 동일 가격 동점자 브랜드가 다수 존재할 때 발생할 수 있는 데이터 왜곡 및 특정 데이터 몰림 현상을 방지하기 위해 정교한 정렬 체인을 빌드했습니다.
- **우선순위 수식 알고리즘**: Absolute Lowest Price (가격 최저값 최우선) -> Picked Brands Exclusion (이미 특정 카테고리에서 선정된 브랜드 가중치 제외 처리로 골고루 분산 노출) ➡️ Alphabetical Brand Name (최종 동점 시 브랜드명 사전식 순 정렬) 메커니즘을 자바 람다 스트림의 커스텀 비교기(Comparator) 인터페이스를 활용하여 깔끔한 모던 코드로 구현했습니다.

### 3. 📉 Querydsl 컴파일 타임 최적화 및 복잡한 Extremum 서브쿼리 박멸
- 단일 카테고리 내에서 최저가 상품 정보와 최고가 상품 정보를 동시에 뽑아 한 번에 리턴해야 하는 고도화된 스펙을 만족하기 위해 ProductsRepositoryImpl에 Querydsl Custom 레포지토리 아키텍처를 도입했습니다.
- 서브쿼리용 엔티티 인스턴스(subProductsEntity)를 내부 격리 구조로 동적 빌드하고, 연관 도메인 간의 깊은 참조 레이어에 fetchJoin()을 명시적으로 매핑하여 **JPA 최대의 고질병인 N+1 쿼리 오버헤드를 완벽히 차단**하고 단 한 번의 인덱스 스캔 쿼리로 타깃 셋을 바인딩합니다.

---

## Infrastructure & DevOps (인프라 및 부하 테스트)

```text
 [ k6 Load Test Container ] ──(초당 500회 일정한 요청 타격)──> [ Spring Boot Application ]
                                                                        │ (Actuator 수집)
                                                                        ▼
 [ Grafana Dashboard ] <─── [ Prometheus Timeseries DB ] <─── /actuator/prometheus
Docker-compose 멀티 컨테이너 네트워크: 로컬 애플리케이션 컴포넌트, 시계열 메트릭 수집기(Prometheus), 시각화 대시보드(Grafana) 인프라 전체를 단 하나의 오케스트레이션 네트워크 벨트로 묶어 이식성과 인프라 독립성을 극대화했습니다.

고도화된 k6 성능 테스트 파이프라인 수행:

load-test.js: 동적 유저 커브(VUs 가상 사용자 점진적 증가) 시나리오 기반의 인프라 체력 테스트 가동.

open-model.js: 시스템 가용성을 정밀 분석하기 위해 가상 유저의 응답 대기 지연에 구애받지 않고 초당 타깃 트래픽을 일정하게 밀어붙이는 오픈 모델 도착률 제어 아키텍처(constant-arrival-rate)를 설계하여 초당 500 RPS(Requests Per Second) 환경에서의 인프라 내구성을 검증했습니다.

서버 매트릭 인프라 동기화: micrometer-registry-prometheus 엔진 프로바이더를 결합하여 내장 톰캣 서버의 가용 스레드 풀(tomcat.threads.config), JVM 힙 영역 메모리 스왑 상태, HikariCP 커넥션 풀 등의 코어 정보를 데이터 시계열로 실시간 추적하고 시각화 대시보드를 연동 완료했습니다.

Package Structure (디렉토리 및 소스 구조)

Plaintext
src/main/java/org/example/cloth_shopping_mall
├── component        # DataInitializer (장부 엔티티 초기 연동 컴포넌트)
├── controller       # CustomerController, AdminController (REST 엔드포인트 명세)
├── dto              # ProductDto (Record 타입을 적극 활용한 불변 객체 통신 아키텍처)
├── entity           # BrandEntity, ProductsEntity, BrandCategoryLowestPriceEntity (도메인 모델)
├── global           # 공통 모듈 (ApiResponse 공통 포맷, Querydsl 및 글로벌 예외 핸들링 레이어)
├── repository       # Spring Data JPA 인터페이스 및 Querydsl 구현 클래스 계층
└── service          # CustomerService, AdminService (트랜잭션 및 비즈니스 연산 코어)
    API Endpoints Specifications (주요 API 규격)
1. 고객 전용 API 명세 (/api/customer/*)
GET /api/customer/lowest/each

서비스 내 모든 카테고리의 최저가 브랜드 상품 목록과 해당 상품들의 총액 합산 레이아웃 반환 (메모리 로컬 캐싱 적용)

GET /api/customer/lowest/same-brand

단일 브랜드 몰아 사기 시나리오 대응: 하나의 브랜드 안에서 전 카테고리를 일괄 구매할 때 총액 합계가 전사 최저인 단일 탑 브랜드와 세부 상품셋 추출

GET /api/customer/lowest/category/{categoryName}

특정 카테고리명을 파라미터로 주입받아, 해당 카테고리 내부의 최저가 상품 목록과 최고가 상품 목록을 Querydsl 서브쿼리 조합으로 동시 집계하여 반환

2. 운영 관리자 API 명세 (/api/admin/*)
POST /api/admin/brand : 신규 어페럴 브랜드 라이프사이클 추가 등록

PUT /api/admin/brand/{id} : 기존 등록 브랜드의 마스터 정보 수정

DELETE /api/admin/brand/{id} : 브랜드 정보 제거 및 하위 연관 상품 Cascade 정리

POST /api/admin/product : 특정 브랜드 하위에 속하는 신규 의류 상품 인스턴스 추가 (동시 최저가 실시간 장부 동기화 트리거)

PUT /api/admin/product/{id} : 등록된 상품의 가격 정보 변경 및 옵션 수정

DELETE /api/admin/product/{id} : 상품 완전 삭제 처리 (장부 데이터 롤백 정합성 연동)