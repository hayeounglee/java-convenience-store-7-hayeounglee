package store.view;

import store.model.GiftProduct;
import store.model.Product;
import store.model.receipt.AmountInfo;
import store.model.receipt.GiftProducts;
import store.model.receipt.PurchaseProducts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OutputView {
    public void printStoreMenu() throws IOException {
        System.out.println("안녕하세요. W편의점입니다.\n" +
                "현재 보유하고 있는 상품입니다.\n");

        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md")); //java 파일 경로 입력방법
        String product;

        reader.readLine();

        while ((product = reader.readLine()) != null) {
            String[] str = product.split(",");
            System.out.printf("- %s %s원 %s %s", str[0], String.format("%,d", Integer.parseInt(str[1])), changeNoStock(str[2]), changeNull(str[3])); //null은 재고 없음으로 하기
            System.out.println();
        }
        reader.close();
    }

    public void printPurchaseProduct(PurchaseProducts purchaseProducts) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t\t\t수량\t\t금액");
        for (Product product : purchaseProducts.getPurchaseProducts()) {
            System.out.printf("%-4s\t\t\t\t%s\t\t%s\n", product.getName(), product.getQuantity(), String.format("%,d", product.getQuantity() * product.getPrice()));
        }
    }

    public void printGiftProducts(GiftProducts giftProducts) {
        System.out.println("=============증\t\t정===============");
        for (GiftProduct giftProduct : giftProducts.getGiftProducts()) {
            System.out.printf("%-4s\t\t\t\t%d\n", giftProduct.getName(), giftProduct.getQuantity());
        }
    }

    public void printAmountInfo(AmountInfo amountInfo) {
        System.out.println("====================================");
        System.out.printf("총구매액\t\t\t\t%s\t\t%s", amountInfo.getTotalPurchaseCount(), String.format("%,d", amountInfo.getTotalPurchaseAmount()));
        System.out.println();
        System.out.printf("행사할인\t\t\t\t\t\t%s", String.format("-%,d", amountInfo.getPromotionDiscount()));
        System.out.println();
        System.out.printf("멤버십할인\t\t\t\t\t\t%s", String.format("-%,d", amountInfo.getMembershipDiscount()));
        System.out.println();
        System.out.printf("내실돈\t\t\t\t\t\t%s", String.format("%,d", amountInfo.getPayment()));
        System.out.println();
    }

    private String changeNull(String input) {
        if (input.equals("null")) {
            return "";
        }
        return input;
    }

    private String changeNoStock(String input) {
        if (input.equals("0")) {
            return "재고 없음";
        }
        return input + "개";
    }
}
