package store.model.receipt;

import store.model.GiftProduct;
import store.model.Product;

import java.util.ArrayList;
import java.util.List;

public class GiftProducts {
    private final List<GiftProduct> giftProducts;

    public GiftProducts() {
        giftProducts = new ArrayList<>();
    }

    public void storeGiftProduct(Product product, int num) {
        if (num > 0) {
            giftProducts.add(new GiftProduct(product.getName(), num));
        }
    }

    public List<GiftProduct> getGiftProducts() {
        return giftProducts;
    }
}
