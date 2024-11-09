package store.model.receipt;

import store.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AmountInfo {
    private final static int MEMBERSHIP_DISCOUNT_MAX = 8000;

    private int totalPurchaseAmount = 0;
    private int totalPurchaseCount = 0;
    private int promotionDiscount = 0;
    private int membershipDiscount = 0;

    public void increaseTotal(int quantity, int price) {
        totalPurchaseCount += quantity;
        totalPurchaseAmount += quantity * price;
    }

    public void increasePromotionDiscount(Product product, int giftCount) {
        promotionDiscount += product.getPrice() * giftCount;
    }

    public void calculateMembershipDiscount() {
        DecimalFormat df = new DecimalFormat("#");
        membershipDiscount = Integer.parseInt(df.format(membershipDiscount * 0.3));

        if (membershipDiscount > MEMBERSHIP_DISCOUNT_MAX) {
            membershipDiscount = MEMBERSHIP_DISCOUNT_MAX;
        }
    }

    public void increaseMembershipDiscount(Product product, int giftCount) {
        if (giftCount == 0) {
            membershipDiscount += product.getPrice() * product.getQuantity();
        }
    }

    public int getPayment() {
        return totalPurchaseAmount - (promotionDiscount + membershipDiscount);
    }

    public int getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public int getTotalPurchaseCount() {
        return totalPurchaseCount;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }
}
