package com.example.diabeticmealplannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class GlycemicIndexReference extends AppCompatActivity {

    private ListView listView;
    private Button backButton;
    private ActivityResultLauncher<Intent> backLauncher;

    // HashMaps for carbs and glycemic index
    private HashMap<String, Integer> foodItemsList;
    private HashMap<String, Integer> glycemicIndexList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glycemic_index_reference);

        listView = findViewById(R.id.GIR_list);
        backButton = findViewById(R.id.backButtonGIR);

        backLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> { }
        );

        initializeFoodData();

        ArrayList<String> foodGIList = new ArrayList<>();

        for (String food : foodItemsList.keySet()) {
            int carbs = foodItemsList.get(food);
            int gi = glycemicIndexList.containsKey(food) ? glycemicIndexList.get(food) : -1;
            String item = food + " - Carbs: " + carbs + "g, GI: " + (gi == -1 ? "N/A" : gi);
            foodGIList.add(item);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodGIList);
        listView.setAdapter(adapter);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Features.class);
            backLauncher.launch(intent);
        });
    }

    private void initializeFoodData() {
        foodItemsList = new HashMap<>();
        glycemicIndexList = new HashMap<>();

        // Carbs data
        foodItemsList.put("White Bread", 14);
        foodItemsList.put("Whole Wheat Bread", 14);
        foodItemsList.put("Brown Rice", 45);
        foodItemsList.put("White Rice", 45);
        foodItemsList.put("Basmati Rice", 45);
        foodItemsList.put("Quinoa", 39);
        foodItemsList.put("Cornflakes", 24);
        foodItemsList.put("Oatmeal", 27);
        foodItemsList.put("Instant Oatmeal", 28);
        foodItemsList.put("Sweet Potato", 27);
        foodItemsList.put("Boiled Potato", 37);
        foodItemsList.put("Baked Potato", 37);
        foodItemsList.put("Fries", 38);
        foodItemsList.put("Carrots", 7);
        foodItemsList.put("Pumpkin", 7);
        foodItemsList.put("Peas", 21);
        foodItemsList.put("Corn", 27);

        foodItemsList.put("Apple", 25);
        foodItemsList.put("Banana", 27);
        foodItemsList.put("Orange", 15);
        foodItemsList.put("Grapes", 16);
        foodItemsList.put("Watermelon", 11);
        foodItemsList.put("Mango", 25);
        foodItemsList.put("Pineapple", 22);
        foodItemsList.put("Papaya", 15);
        foodItemsList.put("Strawberries", 12);
        foodItemsList.put("Blueberries", 21);

        foodItemsList.put("Whole Milk", 12);
        foodItemsList.put("Skim Milk", 12);
        foodItemsList.put("Yogurt (Plain)", 12);
        foodItemsList.put("Ice Cream", 23);
        foodItemsList.put("Cheddar Cheese", 1);

        foodItemsList.put("Lentils", 40);
        foodItemsList.put("Chickpeas", 45);
        foodItemsList.put("Kidney Beans", 40);
        foodItemsList.put("Black Beans", 40);
        foodItemsList.put("Green Beans", 7);

        foodItemsList.put("Peanuts", 6);
        foodItemsList.put("Cashews", 9);
        foodItemsList.put("Almonds", 6);
        foodItemsList.put("Walnuts", 4);

        foodItemsList.put("Pizza", 33);
        foodItemsList.put("Hamburger Bun", 25);
        foodItemsList.put("Pasta (white)", 43);
        foodItemsList.put("Whole Wheat Pasta", 37);

        foodItemsList.put("Coke", 39);
        foodItemsList.put("Orange Juice", 26);
        foodItemsList.put("Apple Juice", 28);
        foodItemsList.put("Energy Drink", 29);

        foodItemsList.put("Honey", 17);
        foodItemsList.put("White Sugar", 14);
        foodItemsList.put("Brown Sugar", 12);
        foodItemsList.put("Maple Syrup", 13);

        // Glycemic Index data
        glycemicIndexList.put("White Bread", 75);
        glycemicIndexList.put("Whole Wheat Bread", 69);
        glycemicIndexList.put("Brown Rice", 50);
        glycemicIndexList.put("White Rice", 72);
        glycemicIndexList.put("Basmati Rice", 58);
        glycemicIndexList.put("Quinoa", 53);
        glycemicIndexList.put("Cornflakes", 81);
        glycemicIndexList.put("Oatmeal", 55);
        glycemicIndexList.put("Instant Oatmeal", 79);
        glycemicIndexList.put("Sweet Potato", 61);
        glycemicIndexList.put("Boiled Potato", 78);
        glycemicIndexList.put("Baked Potato", 85);
        glycemicIndexList.put("Fries", 75);
        glycemicIndexList.put("Carrots", 39);
        glycemicIndexList.put("Pumpkin", 75);
        glycemicIndexList.put("Peas", 48);
        glycemicIndexList.put("Corn", 52);

        glycemicIndexList.put("Apple", 38);
        glycemicIndexList.put("Banana", 51);
        glycemicIndexList.put("Orange", 43);
        glycemicIndexList.put("Grapes", 46);
        glycemicIndexList.put("Watermelon", 72);
        glycemicIndexList.put("Mango", 51);
        glycemicIndexList.put("Pineapple", 59);
        glycemicIndexList.put("Papaya", 60);
        glycemicIndexList.put("Strawberries", 41);
        glycemicIndexList.put("Blueberries", 53);

        glycemicIndexList.put("Whole Milk", 31);
        glycemicIndexList.put("Skim Milk", 32);
        glycemicIndexList.put("Yogurt (Plain)", 36);
        glycemicIndexList.put("Ice Cream", 61);
        glycemicIndexList.put("Cheddar Cheese", 0);

        glycemicIndexList.put("Lentils", 32);
        glycemicIndexList.put("Chickpeas", 28);
        glycemicIndexList.put("Kidney Beans", 29);
        glycemicIndexList.put("Black Beans", 30);
        glycemicIndexList.put("Green Beans", 15);

        glycemicIndexList.put("Peanuts", 14);
        glycemicIndexList.put("Cashews", 22);
        glycemicIndexList.put("Almonds", 0);
        glycemicIndexList.put("Walnuts", 0);

        glycemicIndexList.put("Pizza", 60);
        glycemicIndexList.put("Hamburger Bun", 70);
        glycemicIndexList.put("Pasta (white)", 49);
        glycemicIndexList.put("Whole Wheat Pasta", 37);

        glycemicIndexList.put("Coke", 63);
        glycemicIndexList.put("Orange Juice", 50);
        glycemicIndexList.put("Apple Juice", 40);
        glycemicIndexList.put("Energy Drink", 68);

        glycemicIndexList.put("Honey", 58);
        glycemicIndexList.put("White Sugar", 68);
        glycemicIndexList.put("Brown Sugar", 64);
        glycemicIndexList.put("Maple Syrup", 54);
    }
}
