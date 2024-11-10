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
        amountInfo.calculatePromotionDiscount(product, giftCount);
        amountInfo.increaseMembershipDiscount(product, giftCount);

        giftProducts.storeGiftProduct(product, giftCount);
    }

    public void calculateDiscount(boolean isMembershipDiscount) {
        amountInfo.calculateMembershipDiscount(isMembershipDiscount);
    }

    public AmountInfo getAmountInfo() {
        return amountInfo;
    }

    public GiftProducts getGiftProducts() {
        return giftProducts;
    }
}
