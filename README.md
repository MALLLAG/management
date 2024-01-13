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

### Response

- **Status Code:** 201

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

### Response

- **Status Code:** 201

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

### Response

- **Status Code:** 201

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
    "contractPeriod": 2,
    "coverages": [
        {
            "coverageName": "그냥 담보",
            "insuredAmount": 500000,
            "baseAmount": 100
        }
    ]
}
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

### Response

- **Status Code:** 200



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




