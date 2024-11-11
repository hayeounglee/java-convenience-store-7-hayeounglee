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

    private boolean nextPlay;
    private PurchaseProducts purchaseProducts;
    private ReceiptService receiptService;
    private StockManageService stockManageService;

    private int countPurchasePromotion;
    private int countPurchaseNormal;
    private int giftCount;
    private boolean getBenefit;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
        nextPlay = true;
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
        calculateProducts();
        printReceipt();

        if (!repeatUntilAdditionalPlayValid()) {
            nextPlay = false;
        }
    }

    private void calculateProducts() {
        for (Product product : purchaseProducts.getPurchaseProducts()) {
            initCount(product);
            calculateCount(product);

            stockManageService.manageStock(countPurchaseNormal, countPurchasePromotion, product);

            int stockCount = countPurchasePromotion + countPurchaseNormal;

            receiptService.make(stockCount, giftCount, product, getBenefit);
        }
    }

    private void calculateCount(Product product) {
        if (!product.isPromotionDuration()) { //프로모션 기간이 아닐 때
            if(product.isPromotionProduct()){
                getBenefit = false;
            }

            if (product.getNormalStockCount() >= product.getQuantity()) {
                countPurchaseNormal = product.getQuantity();
                return;
            }
            countPurchaseNormal = product.getNormalStockCount();
            countPurchasePromotion = product.getQuantity() - countPurchaseNormal;
            return;
        }

        if (product.isAvailableOnlyPromotion()) {
            calculateCaseOne(product);
            return;
        }
        if (product.isSameWithQuantity()) {
            calculateCaseTwo(product);
            return;
        }
        calculateCaseThree(product);
    }

    private void calculateCaseThree(Product product) {
        countPurchasePromotion = product.getPromotionStockCount();
        countPurchaseNormal = product.getQuantity() - countPurchasePromotion;

        if (product.countPromotionDisable() > 0 & product.isPromotionDuration()) {
            if (!repeatUntilPurchaseValid(product)) {
                countPurchasePromotion = product.buyOnlyPromotion();
                countPurchaseNormal = 0;
            }
            getBenefit = false;
        }
        if (!product.isPromotionDuration() & product.isPromotionProduct()) {
            getBenefit = false;
        }
    }

    private void calculateCaseTwo(Product product) {
        countPurchasePromotion = product.getQuantity();

        if (product.countPromotionDisable() > 0 & product.isPromotionDuration()) {//프로모션 기간, 프로모션 적용 가능
            if (!repeatUntilPurchaseValid(product)) {
                countPurchasePromotion = 0;
            }
            getBenefit = false;
        }
        if (!product.isPromotionDuration() & product.isPromotionProduct()) {
            getBenefit = false;
        }
    }

    private void calculateCaseOne(Product product) {
        countPurchasePromotion = product.getQuantity();

        if (product.canReceiveMoreFreeGift()) { //프로모션 기간, 프로모션 적용 가능
            if (repeatUntilOneMoreFreeValid(product)) {
                countPurchasePromotion += product.getPromotionGetCount();
                giftCount += product.getPromotionGetCount();
                product.increaseQuantity(product.getPromotionGetCount());
                return;
            }
            getBenefit = false;
        }
        if (!product.isPromotionDuration() & product.isPromotionProduct()) {
            getBenefit = false;
        }
    }

    private void generateService() {
        receiptService = new ReceiptService();
        stockManageService = new StockManageService();
    }

    private void initCount(Product product) {
        getBenefit = true;
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
                System.out.println(e.getMessage());
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
