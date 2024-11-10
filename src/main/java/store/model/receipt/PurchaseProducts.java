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
            Product pro = new Product(product);
            validate(pro);
            purchaseProducts.add(pro);
        }
    }

    private void validate(Product product){
        String target = product.getName();
        for (Product pro : purchaseProducts) {
            if (pro.getName().equals(target)) {
                throw new IllegalArgumentException("[ERROR] 중복되는 상품 이름이 있습니다.");
            }
        }
    }

    public List<Product> getPurchaseProducts() {
        return purchaseProducts;
    }
}
