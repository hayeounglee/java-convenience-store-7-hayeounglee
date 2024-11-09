package store;

import store.controller.StoreController;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        StoreController storeController = new StoreController(new InputView(), new OutputView());
        storeController.run();
    }
}
