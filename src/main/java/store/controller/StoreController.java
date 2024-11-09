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

    private int countPurchasePromotion;
    private int countPurchaseNormal;
    private int giftCount;

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
        generateService();
        stockManageService.bringStock();

        outputView.printStoreMenu();
        repeatUntilProductAndPriceValid();

        for (Product product : purchaseProducts.getPurchaseProducts()) {
            initCount(product);
            calculateCount(product);

            stockManageService.manageStock(countPurchaseNormal, countPurchasePromotion, product);

            int stockCount = countPurchasePromotion + countPurchaseNormal;
            receiptService.make(stockCount, giftCount, product);
        }
        printReceipt();

        if (!repeatUntilAdditionalPlayValid()) {
            nextPlay = false;
        }
    }

    private void calculateCount(Product product) {
        if (product.isAvailableOnlyPromotion()) {
            countPurchasePromotion = product.getQuantity();

            if (product.canReceiveMoreFreeGift()) {
                if (repeatUntilOneMoreFreeValid(product)) {
                    countPurchasePromotion += 1;
                    giftCount += 1;
                    product.increaseQuantity();
                }
            }
            return;
        }


        // 프로모션 제품만으로 안되는 경우 + 같은 경우!!!
        countPurchasePromotion = product.getPromotionStockCount();
        countPurchaseNormal = product.getQuantity() - countPurchasePromotion;

        if (product.countPromotionDisable() > 0 & product.isPromotionProduct()) { // 프로모션 혜택을 받지 못하는 수량이 있는 경우
            if (!repeatUntilPurchaseValid(product)) {
                countPurchasePromotion = product.buyOnlyPromotion();
                countPurchaseNormal = 0;
            }
        }


    }

    private void generateService() {
        receiptService = new ReceiptService();
        stockManageService = new StockManageService(); //여기다가 선언해야 하나?
    }

    private void initCount(Product product) {
        countPurchasePromotion = 0;
        countPurchaseNormal = 0;
        giftCount = product.getGiftCount();
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

    private void printReceipt() throws IOException {
        receiptService.calculateDiscount(repeatUntilMembershipDiscountValid());
        outputView.printPurchaseProduct(purchaseProducts);
        outputView.printGiftProducts(receiptService.getGiftProducts());
        outputView.printAmountInfo(receiptService.getAmountInfo());
        stockManageService.reflectStock();
    }
}
