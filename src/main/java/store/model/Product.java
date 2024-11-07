package store.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Product {
    private final String name;
    private final String quantity;
    private String price;

    public Product(String product) throws IOException {
        String[] productInfo = validate(product);
        name = productInfo[0];
        quantity = productInfo[1];
    }

    private String[] validate(String product) throws IOException {
        if (product.isBlank() | product.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        if (!(product.charAt(0) == '[' && product.charAt(product.length() - 1) == ']' && product.contains("-"))) { //"" vs ''의 차이????? String vs char
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        product = product.substring(1, product.length() - 1); // 깔끔하게 파악할 수 있게
        String[] oneProduct = product.split("-", -1);

        for (String one : oneProduct) {
            if (one.isBlank() | one.isEmpty()) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }
        }

        try {
            Integer.parseInt(oneProduct[1]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("[ERROR] 수량은 숫자를 입력해야합니다. 다시 입력해 주세요.");
        }

        if (!isExistProduct(oneProduct[0])) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }

        if (isExceedStock(oneProduct[1])) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
        return oneProduct;
    }

    private boolean isExistProduct(String productName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md")); //java 파일 경로 입력방법
        String food;
        reader.readLine();

        while ((food = reader.readLine()) != null) {
            String foodName = food.split(",")[0];

            if (foodName.equals(productName)) {
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }

    private boolean isExceedStock(String productQuantity) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md")); //java 파일 경로 입력방법
        String food;
        reader.readLine();

        while ((food = reader.readLine()) != null) {
            String foodQuantity = food.split(",")[2];
            if (Integer.parseInt(foodQuantity) < Integer.parseInt(productQuantity)) {
                reader.close();
                return false;
            }

        }
        reader.close();
        return true;
    }

    public boolean isNameMatched(String foodName) {
        if (name.equals(foodName)) {
            return true;
        }
        return false;
    }

    public String getProductPromotion() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md")); //java 파일 경로 입력방법
        String food;

        reader.readLine();

        while ((food = reader.readLine()) != null) {
            String[] foodInfo = food.split(",");

            if (foodInfo[0].equals(name)) {
                if(foodInfo[3]!=null){
                    reader.close();
                    return foodInfo[3];
                }
            }
        }
        reader.close();
        return "noPromotion";
    }

    public String getName(){
        return name;
    }

}

