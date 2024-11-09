package store.model.receipt;

import store.Constant.MembershipDiscount;
import store.model.Product;

import java.text.DecimalFormat;

public class AmountInfo {
    private int totalPurchaseAmount;
    private int totalPurchaseCount;
    private int promotionDiscount;
    private int membershipDiscount;

    public AmountInfo() {
        init();
    }

    private void init() {
        totalPurchaseAmount = 0;
        totalPurchaseCount = 0;
        promotionDiscount = 0;
        membershipDiscount = 0;
    }

    public void increaseTotal(int quantity, int price) {
        totalPurchaseCount += quantity;
        totalPurchaseAmount += quantity * price;
    }

    public void calculatePromotionDiscount(Product product, int giftCount) {
        promotionDiscount += product.getPrice() * giftCount;
    }

    public void calculateMembershipDiscount(boolean isMembershipDiscount) {
        if (!isMembershipDiscount) {
            membershipDiscount = 0;
            return;
        }

        DecimalFormat df = new DecimalFormat("#");
        membershipDiscount = Integer.parseInt(df.format(membershipDiscount * MembershipDiscount.DISCOUNT_RATE.getValue()));

        membershipDiscount = Math.min(membershipDiscount, (int)MembershipDiscount.MAX_DISCOUNT.getValue());
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
