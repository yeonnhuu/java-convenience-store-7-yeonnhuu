# java-convenience-store-precourse

---

# 프로젝트 구조

```plaintext
├── java
│ └── store
│     ├── Application.java
│     ├── controller
│     │ ├── CartManager.java
│     │ └── StoreManager.java
│     ├── domain
│     │ ├── Cart.java
│     │ ├── CartItem.java
│     │ ├── Product.java
│     │ ├── Promotion.java
│     │ └── Receipt.java
│     ├── handler
│     │ ├── ErrorHandler.java
│     │ ├── ProductsFileHandler.java
│     │ ├── PromotionsFileHandler.java
│     │ └── StocksFileHandler.java
│     ├── service
│     │ └── PaymentService.java
│     └── view
│         ├── InputView.java
│         └── OutputView.java
└── resources
├── products.md
├── promotions.md
└── stocks.md
```

---

# 구현할 기능 목록

## 🗂️ Domain

### Product
- 상품의 이름(name), 가격(price), 수량(quantity), 프로모션 이름(promotion)을 정의한다.
- 상품의 속성을 관리한다.
- 상품의 프로모션에 대한 정보를 관리한다.
- 상품의 수량을 관리한다.
- 상품의 고유한 키(상품 이름-프로모션 이름)를 정의한다.

### Promotion
- 프로모션의 이름(name), 구매 수량(buy), 무료 증정 수량(free)을 정의한다.
- 프로모션의 속성을 관리한다.

### CartItem
- 구매할 상품 종류(product), 상품의 총 수량(totalQuantity), 프로모션 세트 수량(promotionSet)을 정의한다.
- 구매할 상품의 속성을 관리한다.
- 구매할 상품의 총 수량, 프로모션 수량, 무료 증정 수량을 계산한다.
- 구매할 상품의 총 금액(할인 이전 금액), 정가 상품 금액, 프로모션 상품 금액, 무료 증정 상품 금액을 계산한다.

### Cart
- 구매할 모든 상품의 총 수량을 계산한다.
- 구매할 모든 상품의 총 금액, 정가 상품 금액, 무료 증정 상품 금액을 계산한다.

### Receipt
- 영수증을 발행할 카트(cart)를 정의한다.
- 총 금액(totalPrice), 프로모션 할인 금액(promotionDiscount), 멤버십 할인 금액(membershipDiscount)을 정의한다.
- 최종 지불 금액(finalPrice)를 계산한다.
- 무료 증정 상품 목록을 구한다.


---

## 👀 View

### InputView
- 구매자의 입력을 처리하는 기능을 제공한다.
    - 구매할 상품명과 수량을 입력받는다.
    - 프로모션 혜택 추가 여부(Y/N)를 입력받는다.
    - 프로모션 재고 부족 시 정가 결제 여부(Y/N)를 입력받는다.
    - 멤버십 할인 여부(Y/N)를 입력받는다.
    - 추가 구매 여부(Y/N)를 입력받는다.

### OutputView
- 편의점 상품 목록을 출력한다.
    - 환영 메시지를 출력한다.
    - 각 상품의 이름, 가격, 재고, 프로모션을 보기 좋게 출력한다.
        - 가격은 천 단위 콤마로 구분하여 출력한다.
        - 재고가 없는 상품은 "재고 없음"으로 출력한다.
        - 프로모션이 없는 상품은 프로모션 정보 없이 ""으로 출력한다.
- 영수증을 구성하여 출력한다.
    - 구매 상품 내역에 상품명, 수량, 가격을 포함한다.
    - 증정 상품 내역에 상품명, 수량을 포함한다.
    - 결제 정보에 총구매액, 행사할인, 멤버십 할인, 최종 결제 금액을 표시한다.

---

## 🕹️ Controller

### StoreManager
- 편의점 프로그램의 흐름을 제어한다.
    - 사용자 입력을 받아 처리하고, 구매 금액을 계산하여 영수증을 생성한다.
    - 결과를 출력하고, 추가 구매 여부에 따라 반복 여부를 결정한다.

### CartManager
- 구매할 상품의 이름과 수량을 검증한다.
    - 구매할 상품이 존재하지 않는 유효성을 검증한다.
    - 구매할 상품의 재고 부족의 유효성을 검증한다.
- 프로모션과 구매 수량에 따른 실행 여부를 확인한다.
    - 추가로 무료 증정 상품을 받을 수 있는 경우 상품 추가 여부를 확인한다.
    - 프로모션 재고가 부족할 경우 정가 구매 여부를 확인한다.


---

## ⚙️ Service

### PaymentService

- 구매할 전체 상품의 목록을 구한다.
- 영수증을 생성한다.
- 멤버십 할인을 관리한다.
    - 멤버십 할인 금액을 계산한다.
    - 멤버십 할인 한도를 계산한다.

---

## 🛠 Handler

### ErrorHandler
- 잘못된 입력 시 오류 메시지를 출력하고 재입력을 유도한다.

### ProductsFileHandler
- `products.md` 파일을 읽어 상품 정보를 불러온다.
    - 파일을 파싱하여 각 상품의 속성을 정의한다.
    - 파일 로드 중 오류 발생 시 `IOException`을 발생시키고 프로그램을 종료한다.

### StocksFileHandler
- `stocks.md` 파일을 관리한다.
    - 파일을 파싱하여 각 상품의 속성을 정의한다.
    - 파일 로드 중 오류 발생 시 `IOException`을 발생시키고 프로그램을 종료한다.
- 상품 정보를 수정하여 `stocks.md`에 저장한다.
    - 모든 상품의 재고를 업데이트하고 품절 상품은 수량을 null로 표시한다.
    - 파일 저장 중 오류 발생 시 `IOException`을 발생시키고 프로그램을 종료한다.

### PromotionsFileHandler
- `promotions.md` 파일을 읽어 프로모션 정보를 불러온다.
    - 파일을 파싱하여 각 프로모션의 속성을 정의한다.
    - 파일 로드 중 오류 발생 시 `IOException`을 발생시키고 프로그램을 종료한다.
- 현재 날짜에 해당하지 않는 프로모션을 필터링한다.
