package store.controller;

import store.model.Product;
import store.model.receipt.AmountInfo;
import store.model.receipt.GiftProducts;
import store.model.receipt.PurchaseProducts;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() throws IOException {
        AmountInfo amountInfo = new AmountInfo();
        GiftProducts giftProducts = new GiftProducts();

        outputView.printStoreMenu();
        PurchaseProducts purchaseProducts = new PurchaseProducts(inputView.getProductAndPrice());


        for (Product product : purchaseProducts.getPurchaseProducts()) {

            int countPurchasePromotion = 0;
            int countPurchaseNormal = 0;
            int giftCount = product.getGiftCount();


            if (!product.isAvailableOnlyPromotion()) { // 프로모션 제품만으로 안되는 경우
                countPurchasePromotion = product.getPromotionStockCount();
                countPurchaseNormal = product.getQuantity() - countPurchasePromotion;

                if (product.countPromotionDisable() > 0) { // 프로모션 혜택을 받지 못하는 수량이 있는 경우
                    if (!inputView.getPurchaseOrNot(product.getName(), product.countPromotionDisable())) {
                        countPurchasePromotion = product.buyOnlyPromotion();
                        countPurchaseNormal = 0;

                    }
                }
                // 재고 감소 시킨다

            }

            if (product.isAvailableOnlyPromotion()) {
                countPurchasePromotion = product.getQuantity();

                if (product.canReceiveMoreFreeGift()) {
                    if (inputView.getOneMoreFree(product)) {
                        countPurchasePromotion += 1;
                        giftCount += 1;
                    }
                }
                // 재고 감소 시킨다


            }

            int stockCount = countPurchasePromotion + countPurchaseNormal;
            amountInfo.increaseTotal(stockCount, product.getPrice());
            amountInfo.increasePromotionDiscount(product, giftCount);
            amountInfo.increaseMembershipDiscount(product, giftCount);//타이밍이 멤버십 할인 할 거냐 하기 전에 이미 계산되는게 아쉬움..

            giftProducts.storeGiftProduct(product, giftCount);

        }
        if (inputView.getMembershipDiscountOrNot()) {
            amountInfo.calculateMembershipDiscount(giftProducts);
        }
//
//        if (inputView.getAdditionalPurchase()) {
//            //게임 다시 시작
//        }
    }
}
