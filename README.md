# 실행 및 테스트 방법

## h2 console 접속 정보

`http://localhost:8080/management/api/h2-console`

<img width="475" alt="1" src="https://github.com/kakao-insurance-quiz/20240105-ksh/assets/87420630/e52fb86e-f57c-452b-a612-c20e4f9cd5ea">

## 테스트 방법

```sql
insert into product(name, contract_period_minimum, contract_period_maximum) values ('여행자 보험', 1, 3)
insert into product(name, contract_period_minimum, contract_period_maximum) values ('휴대폰 보험', 1, 12)
insert into coverage(product_id, name, insured_amount, base_amount) values (1, '상해치료비', 1000000, 100)
insert into coverage(product_id, name, insured_amount, base_amount) values (1, '항공기 지연도착시 보상금', 500000, 100)
insert into coverage(product_id, name, insured_amount, base_amount) values (2, '부분손실', 750000, 100)
insert into coverage(product_id, name, insured_amount, base_amount) values (2, '전체손실', 1570000, 40)
```

**import.sql**을 이용하여 서버 시작 시 상품/담보 dummy data가 위와 같이 insert됩니다. <br>
상품/담보 생성 API로 등록을 하고 계약을 진행해도 되고, 이미 등록된 dummy data 상품/담보를 이용하여 계약을 진행할 수도 있습니다.

<br>

### 테스트 예시

1. 상품을 등록한다

```curl
curl --location --request POST 'http://localhost:8080/management/api/products' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "그냥 상품",
  "contractPeriodMinimum": 1,
  "contractPeriodMaximum": 12
}'
```

2. 담보를 등록한다

```curl
curl --location --request POST 'http://localhost:8080/management/api/coverages' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productId": 3,
    "name": "그냥 담보",
    "insuredAmount": "500000",
    "baseAmount": "100"
}'
```

3. 계약을 등록한다

```curl
curl --location --request POST 'http://localhost:8080/management/api/contracts' \
--header 'Authorization: Bearer 1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productId": 3,
    "insuranceStartDate": "2023-12-21",
    "insuranceEndDate": "2024-04-21",
    "coverageIds": [5]
}'
```

4. 계약을 조회, 수정한다

```curl
curl --location --request GET 'http://localhost:8080/management/api/contracts/1'
```

```
curl --location --request PATCH 'http://localhost:8080/management/api/contracts' \
--header 'Content-Type: application/json' \
--data-raw '{
    "contractId": 1,
    "addCoverageIds": [],
    "deleteCoverageIds": [],
    "insuranceEndDate": "2024-07-21",
    "contractStatus": "NORMAL"
}'
```

5. 예상 보험료 계산

```curl
curl --location --request GET 'http://localhost:8080/management/api/contracts/estimate?productId=1&contractPeriod=6&coverageIds=1,2'
```

<br>
<br>
<br>


# 상품 API

## 상품 생성

### Request

- **Method:** POST
- **Endpoint:** `/management/api/products`
- **Description:** 상품을 생성합니다.

#### Request Body

```json
{
  "name": "그냥 상품",
  "contractPeriodMinimum": 1,
  "contractPeriodMaximum": 12
}
```

```
name: 상품명
contractPeriodMinimum: 계약 최소 기간
contractPeriodMaximum: 계약 최대 기간
```

### Response

- **Status Code:** 201

```json
{
    "productId": 3
}
```

```
productId: 상품 번호
```

<br>
<br>
<br>

# 담보 API

## 담보 생성

### Request

- **Method:** POST
- **Endpoint:** `/management/api/coverages`
- **Description:** 담보를 생성합니다.

#### Request Body

```json
{
    "productId": 3,
    "name": "그냥 담보",
    "insuredAmount": "500000",
    "baseAmount": "100"
}
```

```
productId: 상품 id
name: 담보명
insuredAmount: 가입 금액
baseAmount: 기준 금액
```

### Response

- **Status Code:** 201

```json
{
    "coverageId": 5
}
```

```
coverageId: 담보 번호
```

<br>
<br>
<br>

# 계약 API

## 계약 생성

### Request

- **Method:** POST
- **Endpoint:** `/management/api/contracts`
- **Description:** 계약을 생성합니다.

#### Request Body

```json
{
    "productId": 3,
    "insuranceStartDate": "2023-12-21",
    "insuranceEndDate": "2024-01-21",
    "coverageIds": [5]
}
```

```
productId: 상품 id
insuranceStartDate: 계약 시작일
insuranceEndDate: 계약 종료일
```

### Response

- **Status Code:** 201

```json
{
    "contractId": 1
}
```

```
contractId: 계약 번호
```

## 계약 조회

### Request

- **Method:** GET
- **Endpoint:** `/management/api/contracts/{contractId}`
- **Description:** 계약을 조회합니다.

### Response 

- **Status Code:** 200

```json
{
    "productName": "그냥 상품",
    "contractPeriod": 5,
    "totalAmount": 25000.00,
    "insuranceStartDate": "2023-12-21",
    "insuranceEndDate": "2024-04-21",
    "contractStatus": "NORMAL",
    "coverages": [
        {
            "coverageName": "그냥 담보",
            "insuredAmount": 500000,
            "baseAmount": 100
        }
    ]
}
```

```
productName: 상품명
contractPeriod: 계약기간
totalAmount: 총 보험료
insuranceStartDate: 계약 시작일
insuranceEndDate: 계약 종료일
contractStatus: 계약 상태
coverageName: 담보명
insuredAmount: 가입 금액
baseAmount: 기준 금액
```

## 계약 수정

### Request

- **Method:** PATCH
- **Endpoint:** `/management/api/contracts`
- **Description:** 계약을 수정합니다.

### Request Body

```json
{
    "contractId": 1,
    "addCoverageIds": [6],
    "deleteCoverageIds": [5],
    "insuranceEndDate": "2024-04-21",
    "contractStatus": "NORMAL"
}
```

```
contractId: 계약 id
addCoverageIds: 추가 담보 id
deleteCoverageIds: 삭제 담보 id
insuranceEndDate: 수정하려는 계약 만료일
contractStatus: 수정하려는 계약 상태
```

### Response

- **Status Code:** 200

```json
{
    "contractId": 1
}
```

```
contractId: 계약 번호
```


## 예상 총 보험료 계산

### Request

- **Method:** GET
- **Endpoint:** `/management/api/contracts/estimate`
- **Description:** 예상 총 보험료를 계산합니다.

#### Request Parameters

- `productId` (Long, 필수): 상품 ID
- `contractPeriod` (Int, 필수): 계약 기간
- `coverageIds` (List<Long>, 필수): 선택한 담보 ID 목록

### Response

- **Status Code:** 200

```json
{
    "estimateAmount": 90000.00
}
```

```
estimateAmount: 예상 보험료
```

<br>
<br>
<br>

# 안내장 발송 기능

```kotlin
    @Query(
        value = """
            SELECT * 
            FROM contract c 
            WHERE c.insurance_end_date = TIMESTAMPADD(DAY, 7, CURRENT_DATE)
        """,
        nativeQuery = true
    )
    fun findExpiringContractsOneWeekBefore(): List<Contract>
```

계약 만료일이 7일 남은 계약들을 조회하여, 안내장을 발송합니다. *(시스템 출력으로 대체하였습니다.)*




