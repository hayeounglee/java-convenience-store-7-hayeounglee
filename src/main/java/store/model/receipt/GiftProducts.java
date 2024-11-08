package store.model.receipt;

import store.model.GiftProduct;
import store.model.Product;

import java.util.ArrayList;
import java.util.List;

public class GiftProducts {
    private List<GiftProduct> giftProducts;

    public void GiftProduct() {
        giftProducts = new ArrayList<>();
    }

    public void storeGiftProduct(Product product, int num) {
        giftProducts.add(new GiftProduct(product.getName(), num));
    }

    public GiftProducts() {
        giftProducts = new ArrayList<>();
    }

}
