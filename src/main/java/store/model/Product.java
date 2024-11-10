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
    private int promotionStockCount = 0;
    private int normalStockCount = 0;
    private String promotion = "null";
    private int promotionCount = 0;

    public Product(String product) throws IOException {
        String[] productInfo = validate(product);
        name = productInfo[0];
        quantity = Integer.parseInt(productInfo[1]);
    }

    private String[] validate(String product) throws IOException {
        validateForm(product);

        String[] oneProduct = extractOneProduct(product);

        validateOneProductForm(oneProduct);
        validateName(oneProduct);
        validateQuantity(oneProduct);

        return oneProduct;
    }

    private String[] extractOneProduct(String product) {
        String inputProduct = product.substring(1, product.length() - 1); // 깔끔하게 파악할 수 있게
        return inputProduct.split("-", -1);
    }

    private void validateForm(String product) {
        if (product.isBlank() | product.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        if (!(product.charAt(0) == '[' && product.charAt(product.length() - 1) == ']' && product.contains("-"))) { //"" vs ''의 차이????? String vs char
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private void validateOneProductForm(String[] oneProduct) {
        for (String one : oneProduct) {
            if (one.isBlank() | one.isEmpty()) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }
        }
    }

    private void validateName(String[] oneProduct) throws IOException {
        if (!isExistProduct(oneProduct[0])) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }

    private boolean isExistProduct(String productName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md")); //java 파일 경로 입력방법
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

    private void validateQuantity(String[] oneProduct) throws IOException {
        try {
            Integer.parseInt(oneProduct[1]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("[ERROR] 수량은 숫자를 입력해야합니다. 다시 입력해 주세요.");
        }

        if (Integer.parseInt(oneProduct[1]) <= 0) {
            throw new IllegalArgumentException("[ERROR] 수량은 1이상의 숫자를 입력해야 합니다. 다시 입력해 주세요.");
        }

        if (isStockLack(oneProduct)) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    private boolean isStockLack(String[] productInfo) throws IOException {
        findProductStock(productInfo);
        return promotionStockCount + normalStockCount < Integer.parseInt(productInfo[1]);
    }

    private void findProductStock(String[] productInfo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md")); //java 파일 경로 입력방법
        String food;

        reader.readLine();
        while ((food = reader.readLine()) != null) {
            String foodName = food.split(",")[0];
            if (foodName.equals(productInfo[0])) {
                updateStock(food);
            }
        }
        reader.close();
    }

    private void updateStock(String food) throws IOException {
        String foodQuantity = food.split(",")[2];
        String foodPromotion = food.split(",")[3];

        if (foodPromotion.equals("null")) {
            normalStockCount += Integer.parseInt(foodQuantity);
        }//마지막에 수행하는 걸로 수정하는 거 어떤가 싶음
        promotionCount = getPromotionCount(foodPromotion);
        if (promotionCount != 0) {
            promotion = foodPromotion;
            promotionStockCount += Integer.parseInt(foodQuantity);
            //return
        }
    }

    public boolean isAvailableOnlyPromotion() {
        return promotionStockCount > quantity;
    }

    public int countPromotionDisable() {
        return decreasePromotionStock() + decreaseNormalStock();
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
        if (remainProduct == (promotionCount - 1)) {
            return (promotionStockCount - quantity) >= 1;
        }
        return false;
    }

    public int getPromotionCount(String promotionName) throws IOException {
        // if (promotion.equals("null")) return 0;
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/promotions.md")); //java 파일 경로 입력방법
        String readPromotion;

        reader.readLine();
        while ((readPromotion = reader.readLine()) != null) {
            String[] promotionInfo = readPromotion.split(",");
            if (promotionInfo[0].equals(promotionName) & isValidateDate(promotionInfo[3], promotionInfo[4])) {
                reader.close();
                return Integer.parseInt(promotionInfo[1]) + Integer.parseInt(promotionInfo[2]); //각각 parse해야 하나?
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

    public void increaseQuantity() {
        quantity += 1;
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
}

