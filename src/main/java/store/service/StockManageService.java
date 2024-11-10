package store.service;

import store.model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        boolean noStock = true;

        reader.readLine();
        if (reader.readLine() == null) {
            throw new IOException();
        }

        while ((line = reader.readLine()) != null) {
            foods.add(line);
            if (!line.split(",")[2].equals("0")) {
                noStock = false;
            }
        }

        if (noStock) {
            throw new IOException("[ERROR] 재고 문제 발생");
        }

        reader.close();
    }

    public void manageStock(int countPurchaseNormal, int countPurchasePromotion, Product product) {
        String targetName = product.getName();

        for (int i = 0; i < foods.size(); i++) {
            String[] food = foods.get(i).split(",");
            if (food[0].equals(targetName)) {
                int newStock = updateStock(food, countPurchaseNormal, countPurchasePromotion);
                String updateStock = food[0] + "," + food[1] + "," + newStock + "," + food[3];
                foods.set(i, updateStock);
            }
        }
    }

    private int updateStock(String[] food, int countPurchaseNormal, int countPurchasePromotion) {
        int currentSock = Integer.parseInt(food[2]);
        if (food[3].equals("null")) {
            return currentSock - countPurchaseNormal;
        }
        return currentSock - countPurchasePromotion;
    }

    public void reflectStock() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/products.md", false));

        for (String updatedLine : foods) {
            writer.write(updatedLine + "\r\n");
        }
        writer.close();
    }
}
