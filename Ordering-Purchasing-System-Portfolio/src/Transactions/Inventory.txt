package Transactions;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<String, Integer> inventory;
    private final Map<String, Double> itemPrices;

    public Inventory() {
        inventory = new HashMap<>();
        itemPrices = new HashMap<>();
        loadDefaultInventory();
    }

    private void loadDefaultInventory() {
        // Adding default items to the inventory with their quantities and prices
        inventory.put("Apples", 50);
        itemPrices.put("Apples", 0.5);

        inventory.put("Oranges", 40);
        itemPrices.put("Oranges", 0.75);

        inventory.put("Bananas", 30);
        itemPrices.put("Bananas", 0.3);

        inventory.put("Grapes", 20);
        itemPrices.put("Grapes", 2.0);

        inventory.put("Strawberries", 25);
        itemPrices.put("Strawberries", 3.5);

        inventory.put("Blueberries", 15);
        itemPrices.put("Blueberries", 4.0);

        inventory.put("Tomatoes", 60);
        itemPrices.put("Tomatoes", 0.9);

        inventory.put("Potatoes", 80);
        itemPrices.put("Potatoes", 0.4);

        inventory.put("Carrots", 70);
        itemPrices.put("Carrots", 0.6);

        inventory.put("Lettuce", 35);
        itemPrices.put("Lettuce", 1.2);

        inventory.put("Onions", 55);
        itemPrices.put("Onions", 0.7);

        inventory.put("Cucumbers", 45);
        itemPrices.put("Cucumbers", 0.8);

        inventory.put("Pineapples", 18);
        itemPrices.put("Pineapples", 2.5);

        inventory.put("Watermelons", 12);
        itemPrices.put("Watermelons", 3.0);

        inventory.put("Peppers", 33);
        itemPrices.put("Peppers", 1.1);

        inventory.put("Mushrooms", 28);
        itemPrices.put("Mushrooms", 1.8);

        inventory.put("Spinach", 38);
        itemPrices.put("Spinach", 2.2);

        inventory.put("Broccoli", 26);
        itemPrices.put("Broccoli", 1.9);

        inventory.put("Zucchini", 32);
        itemPrices.put("Zucchini", 0.95);

        inventory.put("Eggplants", 22);
        itemPrices.put("Eggplants", 1.5);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public Map<String, Double> getItemPrices() {
        return itemPrices;
    }

    public void updateInventory(String item, int quantityChange) {
        inventory.put(item, inventory.getOrDefault(item, 0) + quantityChange);
    }
}
