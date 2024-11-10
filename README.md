## 입력

- 입력값 전체 공통 예외처리
    - [x]  빈값 또는 null값 입력시

- **구매할 상품과 수량**
    - 상품명,수량은 하이픈(-)으로
    - 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분
    - `구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])`
    - 예외처리
        - `,`으로 split한 후 각각에 대해서…
            - [x]  []이 ‘양쪽 끝에’ 각각 없으면
            - [x]  -이 없으면

          → 빈문자열인지도 함께 확인이 가능

        - -으로 split한 후 각각에 대해서…
            - [ ]  (스스로 판단한 내용) 상품 이름 중복 입력시
            - [x]  판매 상품이 아니면
            - [x]  재고 수량을 초과했으면
                - 프로모션 재고가 부족할 경우에는 일반 재고를 사용함
            - [x]  수량을 숫자 아닌 자료형으로 입력하면
            - [x]  수량을 0이하 입력하면

- Y/N 공통 예외처리
    - [x]  `Y` 또는 `N` 이 아닌 값을 입력했을 때
- **증정 받을 수 있는 상품 추가 여부**
    - 프로모션이 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우,
    - 그 수량만큼 추가 여부를 입력받기
    - `현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)`
- **정가로 결제할지에 대한 여부**
    - 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우,
    - 일부 수량에 대해 정가로 결제할지 여부를 입력받기
    - `현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)`
    - 스스로 판단한 내용
        - `N`를 입력시 : 구매목록에서 제외
- **멤버십 할인 적용 여부**
    - `멤버십 할인을 받으시겠습니까? (Y/N)`
    - 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
    - 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용한다.
    - 멤버십 할인의 최대 한도는 8,000원이다.
- **추가 구매 여부**
    - `감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)`

## 출력

- 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다. 만약 재고가 0개라면 `재고 없음`을 출력
    - **천단위 쉼표 찍기**

    ```java
    안녕하세요. W편의점입니다.
    현재 보유하고 있는 상품입니다.
    
    - 콜라 1,000원 10개 탄산2+1
    - 콜라 1,000원 10개
    - 사이다 1,000원 8개 탄산2+1
    - 사이다 1,000원 7개
    - 오렌지주스 1,800원 9개 MD추천상품
    - 오렌지주스 1,800원 재고 없음
    - 탄산수 1,200원 5개 탄산2+1
    - 탄산수 1,200원 재고 없음
    - 물 500원 10개
    - 비타민워터 1,500원 6개
    - 감자칩 1,500원 5개 반짝할인
    - 감자칩 1,500원 5개
    - 초코바 1,200원 5개 MD추천상품
    - 초코바 1,200원 5개
    - 에너지바 2,000원 5개
    - 정식도시락 6,400원 8개
    - 컵라면 1,700원 1개 MD추천상품
    - 컵라면 1,700원 10개
    ```


- 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력
    - **천단위 쉼표 찍기**

    ```java
    ==============W 편의점================
    상품명		수량	금액
    콜라		    3 	3,000
    에너지바 		5 	10,000
    =============증	정===============
    콜라		1
    ====================================
    총구매액		   8	13,000
    행사할인			-1,000
    멤버십할인			-3,000
    내실돈			 9,000
    ```


- 에러 메시지
    - 사용자가 잘못된 값을 입력했을 때, **"[ERROR]"로 시작**하는 오류 메시지와 함께 상황에 맞는 안내를 출력한다.
        - 구매할 상품과 수량 형식이 올바르지 않은 경우: `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
        - 존재하지 않는 상품을 입력한 경우: `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`
        - 구매 수량이 재고 수량을 초과한 경우: `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.`
        - 기타 잘못된 입력의 경우: `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`

- 사용자가 잘못된 값을 입력할 경우 `IllegalArgumentException`를 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.
    - `Exception`이 아닌 `IllegalArgumentException`, `IllegalStateException` 등과 같은 명확한 유형을 처리한다.

## 로직

## 추가 라이브러리

- `camp.nextstep.edu.missionutils`에서 제공하는 `DateTimes` 및 `Console` API를 사용하여 구현해야 한다.
    - 현재 날짜와 시간을 가져오려면 `camp.nextstep.edu.missionutils.DateTimes`의 `now()`를 활용한다.
    - 사용자가 입력하는 값은 `camp.nextstep.edu.missionutils.Console`의 `readLine()`을 활용한다.

- 테스트할 때 `products.md`

```java
name,price,quantity,promotion
콜라,1000,10,탄산2+1
콜라,1000,10,null
사이다,1000,8,탄산2+1
사이다,1000,7,null
오렌지주스,1800,9,MD추천상품
오렌지주스,1800,0,null
탄산수,1200,5,탄산2+1
탄산수,1200,0,null
물,500,10,null
비타민워터,1500,6,null
감자칩,1500,5,반짝할인
감자칩,1500,5,null
초코바,1200,5,MD추천상품
초코바,1200,5,null
에너지바,2000,5,null
정식도시락,6400,8,null
컵라면,1700,1,MD추천상품
컵라면,1700,10,null

```

- 요구사항에 나와 있는 거 테스트할 때 products.md

```java
name,price,quantity,promotion
콜라,1000,10,탄산2+1
콜라,1000,10,null
사이다,1000,8,탄산2+1
사이다,1000,7,null
오렌지주스,1800,9,MD추천상품
탄산수,1200,5,탄산2+1
물,500,10,null
비타민워터,1500,6,null
감자칩,1500,5,반짝할인
감자칩,1500,5,null
초코바,1200,5,MD추천상품
초코바,1200,5,null
에너지바,2000,5,null
정식도시락,6400,8,null
컵라면,1700,1,MD추천상품
컵라면,1700,10,null
```
