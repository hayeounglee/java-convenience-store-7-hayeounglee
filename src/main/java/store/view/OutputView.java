package store.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
            System.out.printf("- %s %s원 %s개 %s", str[0], String.format("%,d", Integer.parseInt(str[1])), str[2], str[3]); //null은 재고 없음으로 하기
            System.out.println();
        }
        reader.close();
    }


    public void printReceipt(List<String> result) throws IOException {
        printPurchaseProduct();
        printAmountInfo(result);
    }

    private void printPurchaseProduct() throws IOException {
        System.out.println("==============W 편의점================\n" + "상품명\t\t수량\t금액");

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/receipt.md")); //java 파일 경로 입력방법
        String product;
        reader.readLine();

        while ((product = reader.readLine()) != null) {
            String[] str = product.split(",");
            System.out.printf("%s\t\t%s\t%s", str[0], str[1], String.format("%,d", Integer.parseInt(str[2]))); //null은 재고 없음으로 하기
            System.out.println();
        }
        reader.close();

        System.out.println("=============증\t정===============");

    }

    private void printAmountInfo(List<String> result) {
        System.out.println("====================================");
        System.out.printf("총구매액\t\t%s\t%s", result.get(0), String.format("%,d", Integer.parseInt(result.get(1))));
        System.out.println();
        System.out.printf("행사할인\t\t%s", String.format("%,d", Integer.parseInt(result.get(2))));
        System.out.println();
        System.out.printf("멤버십할인\t\t%s", String.format("%,d", Integer.parseInt(result.get(3))));
        System.out.println();
        System.out.printf("내실돈\t\t%s", String.format("%,d", Integer.parseInt(result.get(4))));

    }
}
