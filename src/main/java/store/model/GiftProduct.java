package store.model;

public class GiftProduct {
    private final String name;
    private final int quantity;

    public GiftProduct(String productName, int quantity) {
        name = productName;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
