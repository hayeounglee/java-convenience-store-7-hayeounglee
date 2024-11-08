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
    private int payment = 0;


    public AmountInfo() {

    }

    public void increaseTotal(int quantity, int price) {
        totalPurchaseCount += quantity;
        totalPurchaseAmount += quantity * price;
    }

    public void increasePromotionDiscount(Product product, int giftCount) {
        promotionDiscount += product.getPrice() * giftCount;
    }

    public void calculateMembershipDiscount(GiftProducts giftProducts) {
        DecimalFormat df = new DecimalFormat("#");
        membershipDiscount = Integer.parseInt(df.format(membershipDiscount * 0.3));

        if (membershipDiscount > MEMBERSHIP_DISCOUNT_MAX) {
            membershipDiscount = MEMBERSHIP_DISCOUNT_MAX;
        }
        System.out.println(membershipDiscount);
    }

    public void increaseMembershipDiscount(Product product, int giftCount) {
        if (giftCount == 0) {
            membershipDiscount += product.getPrice() * product.getQuantity();
        }

    }

    private void calculatePayment() {
        payment = totalPurchaseAmount - (promotionDiscount + membershipDiscount);
    }
}
