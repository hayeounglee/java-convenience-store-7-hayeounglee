package store.service;

import store.model.Product;
import store.model.receipt.AmountInfo;
import store.model.receipt.GiftProducts;

public class ReceiptService {
    private final AmountInfo amountInfo;
    private final GiftProducts giftProducts;

    public ReceiptService() {
        amountInfo = new AmountInfo();
        giftProducts = new GiftProducts();
    }

    public void make(int stockCount, int giftCount, Product product) {
        amountInfo.increaseTotal(stockCount, product.getPrice());
        amountInfo.increasePromotionDiscount(product, giftCount);
        amountInfo.increaseMembershipDiscount(product, giftCount);//타이밍이 멤버십 할인 할 거냐 하기 전에 이미 계산되는게 아쉬움..

        giftProducts.storeGiftProduct(product, giftCount);
    }

    public void calculateDiscount() {
        amountInfo.calculateMembershipDiscount();
    }

    public AmountInfo getAmountInfo() {
        return amountInfo;
    }

    public GiftProducts getGiftProducts() {
        return giftProducts;
    }
}
