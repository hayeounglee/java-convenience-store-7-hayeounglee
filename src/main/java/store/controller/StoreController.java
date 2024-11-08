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
        int additionCount = 0;
        AmountInfo amountInfo = new AmountInfo();
        GiftProducts giftProducts = new GiftProducts();

        outputView.printStoreMenu();
        PurchaseProducts purchaseProducts = new PurchaseProducts(inputView.getProductAndPrice());


        for (Product product : purchaseProducts.getPurchaseProducts()) {
            amountInfo.increaseTotal(product.getQuantity(), product.getPrice());


        }
    }
}
}
