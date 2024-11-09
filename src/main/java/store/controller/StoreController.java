package store.controller;

import store.model.Product;
import store.model.receipt.PurchaseProducts;
import store.service.ReceiptService;
import store.service.StockManageService;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;

public class StoreController {
    private final InputView inputView;
    private final OutputView outputView;

    private boolean nextPlay = true;
    private PurchaseProducts purchaseProducts;
    private ReceiptService receiptService;
    private StockManageService stockManageService;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() throws IOException {
        while (nextPlay) {
            play();
        }
    }

    private void play() throws IOException {
        receiptService = new ReceiptService();
        stockManageService = new StockManageService();
        stockManageService.bringStock();

        outputView.printStoreMenu();
        repeatUntilProductAndPriceValid();

        for (Product product : purchaseProducts.getPurchaseProducts()) {
            int countPurchasePromotion = 0;
            int countPurchaseNormal = 0;
            int giftCount = product.getGiftCount();

            if (!product.isAvailableOnlyPromotion()) { // 프로모션 제품만으로 안되는 경우 + 같은 경우!!!
                countPurchasePromotion = product.getPromotionStockCount();
                countPurchaseNormal = product.getQuantity() - countPurchasePromotion;

                if (product.countPromotionDisable() > 0 & product.isPromotionProduct()) { // 프로모션 혜택을 받지 못하는 수량이 있는 경우
                    if (!repeatUntilPurchaseValid(product)) {
                        countPurchasePromotion = product.buyOnlyPromotion();
                        countPurchaseNormal = 0;
                    }
                }
            }

            if (product.isAvailableOnlyPromotion()) {
                countPurchasePromotion = product.getQuantity();

                if (product.canReceiveMoreFreeGift()) {
                    if (repeatUntilOneMoreFreeValid(product)) {
                        countPurchasePromotion += 1;
                        giftCount += 1;
                        product.increaseQuantity();
                    }
                }
            }

            // 재고 감소 시킨다
            stockManageService.manageStock(countPurchaseNormal, countPurchasePromotion, product);

            int stockCount = countPurchasePromotion + countPurchaseNormal;
            receiptService.make(stockCount, giftCount, product);
        }

        receiptService.calculateDiscount(repeatUntilMembershipDiscountValid());
        printReceipt();
        stockManageService.reflectStock();

        if (!repeatUntilAdditionalPlayValid()) {
            nextPlay = false;
        }
    }

    private void repeatUntilProductAndPriceValid() {
        while (true) {
            try {
                purchaseProducts = new PurchaseProducts(inputView.getProductAndPrice());
                return;
            } catch (IllegalArgumentException | IOException e) {
                System.out.println(e.getMessage()); //왜 이렇게 가져와야 하지?
            }
        }
    }

    private boolean repeatUntilPurchaseValid(Product product) {
        while (true) {
            try {
                return inputView.getPurchaseOrNot(product.getName(), product.countPromotionDisable());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean repeatUntilOneMoreFreeValid(Product product) {
        while (true) {
            try {
                return inputView.getOneMoreFree(product);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean repeatUntilMembershipDiscountValid() {
        while (true) {
            try {
                return inputView.getMembershipDiscountOrNot();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean repeatUntilAdditionalPlayValid() {
        while (true) {
            try {
                return inputView.getAdditionalPurchase();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printReceipt() {
        outputView.printPurchaseProduct(purchaseProducts);
        outputView.printGiftProducts(receiptService.getGiftProducts());
        outputView.printAmountInfo(receiptService.getAmountInfo());
    }
}
