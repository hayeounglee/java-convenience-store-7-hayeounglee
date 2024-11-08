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

            int decreasePromotionStock = 0;
            int decreaseNormalStock = 0;
            int giftCount = product.getGiftCount();

            if (!product.isAvailableOnlyPromotion()) {
                if (product.countPromotionDisable() > 0) {
                    if (!inputView.getPurchaseOrNot(product.getName(), product.countPromotionDisable())) {
                        decreasePromotionStock += product.decreasePromotionStock();
                        decreaseNormalStock += product.decreaseNormalStock();
                    }
                }
                // 재고 감소 시킨다
                decreasePromotionStock = product.getPromotionStockCount();
                decreaseNormalStock = product.getQuantity() - decreasePromotionStock;
            }

            if (product.isAvailableOnlyPromotion()) {
                if (product.canReceiveMoreFreeGift()) {
                    if (inputView.getOneMoreFree(product)) {
                        decreasePromotionStock += 1;
                        giftCount += 1;
                    }
                }
                // 재고 감소 시킨다
                decreasePromotionStock = product.getPromotionStockCount();
                decreaseNormalStock = product.getQuantity() - decreasePromotionStock;
            }
            int stockCount = decreasePromotionStock + decreaseNormalStock;
            amountInfo.increaseTotal(stockCount, product.getPrice());

            giftProducts.storeGiftProduct(product, giftCount);
            amountInfo.increasePromotionDiscount(product, giftCount);
        }

        // amountInfo.calculateMembershipDiscount(inputView.getMembershipDiscountOrNot());
//
//        if (inputView.getAdditionalPurchase()) {
//            //게임 다시 시작
//        }
    }
}
