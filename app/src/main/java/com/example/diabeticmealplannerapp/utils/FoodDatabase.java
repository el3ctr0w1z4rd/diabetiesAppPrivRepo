package com.example.diabeticmealplannerapp.utils;

import com.example.diabeticmealplannerapp.models.FoodItem;
import java.util.HashMap;
import java.util.Map;

public class FoodDatabase {
    private static Map<String, FoodItem> foodItems;
    
    static {
        initializeFoodDatabase();
    }
    
    private static void initializeFoodDatabase() {
        foodItems = new HashMap<>();
        
        // Grains and Cereals
        foodItems.put("White Bread", new FoodItem("White Bread", 49, 75, 265));
        foodItems.put("Whole Wheat Bread", new FoodItem("Whole Wheat Bread", 41, 69, 247));
        foodItems.put("Brown Rice", new FoodItem("Brown Rice", 77, 50, 370));
        foodItems.put("White Rice", new FoodItem("White Rice", 80, 72, 365));
        foodItems.put("Basmati Rice", new FoodItem("Basmati Rice", 78, 58, 349));
        foodItems.put("Quinoa", new FoodItem("Quinoa", 64, 53, 368));
        foodItems.put("Cornflakes", new FoodItem("Cornflakes", 84, 81, 357));
        foodItems.put("Oatmeal", new FoodItem("Oatmeal", 66, 55, 389));
        foodItems.put("Instant Oatmeal", new FoodItem("Instant Oatmeal", 68, 79, 362));
        
        // Vegetables
        foodItems.put("Sweet Potato", new FoodItem("Sweet Potato", 20, 61, 86));
        foodItems.put("Boiled Potato", new FoodItem("Boiled Potato", 17, 78, 77));
        foodItems.put("Baked Potato", new FoodItem("Baked Potato", 17, 85, 93));
        foodItems.put("Fries", new FoodItem("Fries", 43, 75, 365));
        foodItems.put("Carrots", new FoodItem("Carrots", 10, 39, 41));
        foodItems.put("Pumpkin", new FoodItem("Pumpkin", 7, 75, 26));
        foodItems.put("Peas", new FoodItem("Peas", 14, 48, 81));
        foodItems.put("Corn", new FoodItem("Corn", 19, 52, 86));
        foodItems.put("Green Beans", new FoodItem("Green Beans", 7, 15, 35));
        
        // Fruits
        foodItems.put("Apple", new FoodItem("Apple", 14, 38, 52));
        foodItems.put("Banana", new FoodItem("Banana", 23, 51, 89));
        foodItems.put("Orange", new FoodItem("Orange", 12, 43, 47));
        foodItems.put("Grapes", new FoodItem("Grapes", 16, 46, 62));
        foodItems.put("Watermelon", new FoodItem("Watermelon", 8, 72, 30));
        foodItems.put("Mango", new FoodItem("Mango", 15, 51, 60));
        foodItems.put("Pineapple", new FoodItem("Pineapple", 13, 59, 50));
        foodItems.put("Papaya", new FoodItem("Papaya", 11, 60, 43));
        foodItems.put("Strawberries", new FoodItem("Strawberries", 8, 41, 32));
        foodItems.put("Blueberries", new FoodItem("Blueberries", 14, 53, 57));
        
        // Dairy
        foodItems.put("Whole Milk", new FoodItem("Whole Milk", 5, 31, 61));
        foodItems.put("Skim Milk", new FoodItem("Skim Milk", 5, 32, 34));
        foodItems.put("Yogurt (Plain)", new FoodItem("Yogurt (Plain)", 4, 36, 59));
        foodItems.put("Ice Cream", new FoodItem("Ice Cream", 22, 61, 207));
        foodItems.put("Cheddar Cheese", new FoodItem("Cheddar Cheese", 1, 0, 402));
        
        // Legumes
        foodItems.put("Lentils", new FoodItem("Lentils", 60, 32, 353));
        foodItems.put("Chickpeas", new FoodItem("Chickpeas", 61, 28, 364));
        foodItems.put("Kidney Beans", new FoodItem("Kidney Beans", 60, 29, 333));
        foodItems.put("Black Beans", new FoodItem("Black Beans", 62, 30, 341));
        
        // Nuts
        foodItems.put("Peanuts", new FoodItem("Peanuts", 16, 14, 567));
        foodItems.put("Cashews", new FoodItem("Cashews", 30, 22, 553));
        foodItems.put("Almonds", new FoodItem("Almonds", 22, 0, 579));
        foodItems.put("Walnuts", new FoodItem("Walnuts", 14, 0, 654));
        
        // Processed Foods
        foodItems.put("Pizza", new FoodItem("Pizza", 33, 60, 266));
        foodItems.put("Hamburger Bun", new FoodItem("Hamburger Bun", 51, 70, 294));
        foodItems.put("Pasta (white)", new FoodItem("Pasta (white)", 71, 49, 371));
        foodItems.put("Whole Wheat Pasta", new FoodItem("Whole Wheat Pasta", 62, 37, 348));
        
        // Beverages
        foodItems.put("Coke", new FoodItem("Coke", 11, 63, 42));
        foodItems.put("Orange Juice", new FoodItem("Orange Juice", 10, 50, 45));
        foodItems.put("Apple Juice", new FoodItem("Apple Juice", 11, 40, 46));
        foodItems.put("Energy Drink", new FoodItem("Energy Drink", 11, 68, 45));
        
        // Sweeteners
        foodItems.put("Honey", new FoodItem("Honey", 82, 58, 304));
        foodItems.put("White Sugar", new FoodItem("White Sugar", 100, 68, 387));
        foodItems.put("Brown Sugar", new FoodItem("Brown Sugar", 98, 64, 380));
        foodItems.put("Maple Syrup", new FoodItem("Maple Syrup", 67, 54, 260));
    }
    
    public static FoodItem getFoodItem(String name) {
        return foodItems.get(name);
    }
    
    public static Map<String, FoodItem> getAllFoodItems() {
        return new HashMap<>(foodItems);
    }
    
    public static void addFoodItem(String name, FoodItem foodItem) {
        foodItems.put(name, foodItem);
    }
    
    public static boolean containsFood(String name) {
        return foodItems.containsKey(name);
    }
}