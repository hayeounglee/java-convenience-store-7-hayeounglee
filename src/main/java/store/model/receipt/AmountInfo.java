package store.model.receipt;

import store.model.Product;

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
        totalPurchaseAmount += price;
    }

    public void increasePromotionDiscount(Product product, int giftCount) {
        promotionDiscount += product.getPrice() * giftCount;
    }

    public void calculateMembershipDiscount(boolean isMemberShip) {
        if (isMemberShip) {
            //membershipDiscount = MEMBERSHIP_DISCOUNT_MAX;
        }
    }

    private void calculatePayment() {
        payment = totalPurchaseAmount - (promotionDiscount + membershipDiscount);
    }
}
