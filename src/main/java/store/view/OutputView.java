package store.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OutputView {
    public void printAddMessage() {
        System.out.println("현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
    }

    public void printFixedPriceMessage() {
        System.out.println("현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
    }

    public void printMembershipDiscountMessage() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public void printStoreMenu() throws IOException {
        System.out.println("안녕하세요. W편의점입니다.\n" +
                "현재 보유하고 있는 상품입니다.\n");

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md")); //java 파일 경로 입력방법
        String product;

        reader.readLine();

        while ((product = reader.readLine()) != null) {
            String[] str = product.split(",");
            System.out.printf("- %s %s원 %s개 %s", str[0], String.format(str[1]), str[2], str[3]); //null은 재고 없음으로 하기
            System.out.println();
        }
        reader.close();
    }
}
