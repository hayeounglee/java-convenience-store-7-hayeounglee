package store.service;

import store.model.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StockManageService {
    private final List<String> foods;

    public StockManageService() {
        foods = new ArrayList<>();
    }

    public void bringStock() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/products.md"));
        String line;

        while ((line = reader.readLine()) != null) {
            foods.add(line);
        }
        reader.close();
    }

    public void manageStock(int countPurchaseNormal, int countPurchasePromotion, Product product) {
        String targetName = product.getName();

        for (int i = 0; i < foods.size(); i++) {
            String[] str = foods.get(i).split(",");
            if (str[0].equals(targetName) & !str[3].equals("null")) {
                String updateStock = str[0] + "," + str[1] + "," + Integer.toString(Integer.parseInt(str[2]) - countPurchasePromotion) + "," + str[3];
                foods.set(i, updateStock);
            }
            if (str[0].equals(targetName) & str[3].equals("null")) {
                String updateStock = str[0] + "," + str[1] + "," + Integer.toString(Integer.parseInt(str[2]) - countPurchaseNormal) + "," + str[3];
                foods.set(i, updateStock);
                break;
            }
        }
    }

    public void reflectStock() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/products.md", false));

        for (String updatedLine : foods) {
            writer.write(updatedLine + "\r\n");
        }
        writer.close();
    }
}
