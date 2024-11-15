package store.model;

import camp.nextstep.edu.missionutils.DateTimes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Product {
    private final String name;
    private int quantity;
    private int price;
    private int promotionStockCount;
    private int normalStockCount;
    private String promotion;
    private int promotionCount;
    private int promotionBuyCount;
    private int promotionGetCount;
    private int justPromotionStockCount;

    public Product(String product) throws IOException {
        initCount();
        String[] productInfo = validate(product);
        name = productInfo[0];
        quantity = Integer.parseInt(productInfo[1]);
    }

    private void initCount() {
        promotionStockCount = 0;
        normalStockCount = 0;
        promotion = "null";
        promotionCount = 0;
        promotionBuyCount = 0;
        promotionGetCount = 0;
    }

    private String[] validate(String product) throws IOException {
        if (product.isBlank() | product.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        product = product.replaceAll("\\s", "");

        if (!(product.charAt(0) == '[' && product.charAt(product.length() - 1) == ']' && product.contains("-"))) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        product = product.substring(1, product.length() - 1);
        String[] oneProduct = product.split("-", -1);

        for (String one : oneProduct) {
            if (one.isBlank() | one.isEmpty()) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }
        }

        try {
            Integer.parseInt(oneProduct[1]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        if (Integer.parseInt(oneProduct[1]) <= 0) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        if (!isExistProduct(oneProduct[0])) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }

        if (isStockLack(oneProduct)) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
        return oneProduct;
    }

    private boolean isExistProduct(String productName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md"));
        String food;
        reader.readLine();

        while ((food = reader.readLine()) != null) {
            String foodName = food.split(",")[0];
            String foodPrice = food.split(",")[1];

            if (foodName.equals(productName)) {
                price = Integer.parseInt(foodPrice);
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }

    private boolean isStockLack(String[] productInfo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md"));
        String food;
        reader.readLine();

        while ((food = reader.readLine()) != null) {
            String foodName = food.split(",")[0];
            String foodQuantity = food.split(",")[2];
            String foodPromotion = food.split(",")[3];

            if (foodName.equals(productInfo[0]) & !foodPromotion.equals("null")) {
                promotionCount = getPromotionCount(foodPromotion); // 0이라는 것은 프로모션 기간이 아님
                promotion = foodPromotion; //프로모션 제품
                if (promotionCount != 0) {
                    promotionStockCount += Integer.parseInt(foodQuantity);
                }
                justPromotionStockCount = Integer.parseInt(foodQuantity);
            }
            if (foodName.equals(productInfo[0]) & foodPromotion.equals("null")) {
                normalStockCount += Integer.parseInt(foodQuantity);
            }
        }
        if (justPromotionStockCount + normalStockCount < Integer.parseInt(productInfo[1])) {
            reader.close();
            return true;
        }
        reader.close();
        return false;
    }

    public boolean isAvailableOnlyPromotion() {
        return promotionStockCount > quantity;
    }

    public boolean isSameWithQuantity() {
        return promotionStockCount == quantity;
    }

    public int countPromotionDisable() {
        int count = decreasePromotionStock() + decreaseNormalStock();
        if (count >= promotionBuyCount) {
            return count;
        }
        return 0;
    }

    public int buyOnlyPromotion() {
        quantity = promotionStockCount - decreasePromotionStock();
        return quantity;
    }

    public int decreasePromotionStock() {
        if (promotionCount == 0) return 0;
        return promotionStockCount % promotionCount;
    }

    public int decreaseNormalStock() {
        return quantity - promotionStockCount;
    }

    public int getGiftCount() {
        if (promotionCount == 0) return 0;
        if (isAvailableOnlyPromotion()) {
            return (quantity / promotionCount);
        }
        return (promotionStockCount / promotionCount);
    }

    public boolean canReceiveMoreFreeGift() {
        if (promotionCount == 0) return false;
        int remainProduct = quantity % promotionCount;
        if (remainProduct == (promotionBuyCount)) {
            return (promotionStockCount - quantity) >= promotionGetCount;
        }
        return false;
    }

    public int getPromotionCount(String promotionName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/promotions.md"));
        String readPromotion;

        reader.readLine();

        while ((readPromotion = reader.readLine()) != null) {
            String[] promotionInfo = readPromotion.split(",");
            if (promotionInfo[0].equals(promotionName) & isValidateDate(promotionInfo[3], promotionInfo[4])) {
                promotionBuyCount = Integer.parseInt(promotionInfo[1]);
                promotionGetCount = Integer.parseInt(promotionInfo[2]);
                reader.close();
                return promotionBuyCount + promotionGetCount;
            }
        }
        reader.close();
        return 0;
    }

    private boolean isValidateDate(String start, String end) {
        LocalDateTime targetTime = DateTimes.now();

        String[] startInfo = start.split("-");
        String[] endInfo = end.split("-");

        LocalDate targetDate = LocalDate.of(targetTime.getYear(), targetTime.getMonth(), targetTime.getDayOfMonth());
        LocalDate startDate = LocalDate.of(Integer.parseInt(startInfo[0]), Integer.parseInt(startInfo[1]), Integer.parseInt(startInfo[2]));
        LocalDate endDate = LocalDate.of(Integer.parseInt(endInfo[0]), Integer.parseInt(endInfo[1]), Integer.parseInt(endInfo[2]));
        return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
    }

    public boolean isPromotionProduct() {
        return !promotion.equals("null");
    }
    public boolean isPromotionDuration(){
        return promotionCount!=0;
    }

    public void increaseQuantity(int promotionGetCount) {
        quantity += promotionGetCount;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public int getPromotionStockCount() {
        return promotionStockCount;
    }

    public int getPromotionGetCount() {
        return promotionGetCount;
    }

    public int getNormalStockCount(){
        return normalStockCount;
    }
}
