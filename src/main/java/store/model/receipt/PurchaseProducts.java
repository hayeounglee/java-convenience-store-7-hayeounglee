package store.model.receipt;

import store.model.Product;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class PurchaseProducts {
    private final List<Product> purchaseProducts;

    public PurchaseProducts(String input) throws IOException {
        purchaseProducts = new ArrayList<>();
        validate(input);
    }

    private void validate(String input) throws IOException {
        String[] inputProducts = input.split(",", -1);
        for (String product : inputProducts) {
            purchaseProducts.add(new Product(product));
        }
    }

    public List<Product> getPurchaseProducts() {
        return purchaseProducts;
    }
}
