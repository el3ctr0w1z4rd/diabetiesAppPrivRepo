package com.example.diabeticmealplannerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MealPlanner extends AppCompatActivity {

    private int currentBG;
    private TextView currentBloodGlucose;
    private EditText targetBGText;
    private int targetBG;
    private EditText insulinDoseText;
    private int insulinDose;
    private EditText insulinSensitivityRatioText;
    private int insulinSensitivityRatio;
    private EditText foodText;
    private String food;
    private EditText servingSizeText;
    private double servingSize;
    private Button addMeal;
    private TextView mealListText;
    private ListView mealList;
    private Button backButtonMP;
    public HashMap<String, Integer> foodItemsList = new HashMap<String, Integer>();

    private ArrayList<String> mealItems;
    private ArrayAdapter<String> mealAdapter;

    private ActivityResultLauncher activityResultLauncherMP;

    private int projectedBGAfterMeal;

    private int totalCarbIntake;

    private int carbVal;

    Button saveMeal;
    Button removeMeal;
    private int carbsForServing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealplanner);

        SharedPreferences prefs = getSharedPreferences("bloodGlucoseData", MODE_PRIVATE);
        currentBG = prefs.getInt("value", -1);

        currentBloodGlucose = findViewById(R.id.currentBloodGlucoseValue);
        targetBGText = findViewById(R.id.targetBG);
        insulinDoseText = findViewById(R.id.insulinDose);
        insulinSensitivityRatioText = findViewById(R.id.isr);
        foodText = findViewById(R.id.foodName);
        servingSizeText = findViewById(R.id.servingSize);
        addMeal = findViewById(R.id.addMeal);
        mealListText = findViewById(R.id.mealItems);
        mealList = findViewById(R.id.mealsList); // 🛠️ Moved this before mealAdapter is set
        backButtonMP = findViewById(R.id.backButtonMP);
        saveMeal = findViewById(R.id.saveMeal);
        removeMeal = findViewById(R.id.removeMeal);

//        // Add this in onCreate() after initializing sharedPreferences
//        String savedFoods = sharedPreferences.getString("foodItems", "");
//        if (!savedFoods.isEmpty()) {
//            String[] items = savedFoods.split(",");
//            for (String item : items) {
//                String[] parts = item.split(":");
//                if (parts.length == 2) {
//                    try {
//                        foodItemsList.put(parts[0], Integer.parseInt(parts[1]));
//                    } catch (Exception e) {
//                        Log.e("LoadFoods", "Error loading: " + item);
//                    }
//                }
//            }
//        }

        mealItems = new ArrayList<>();
        mealAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mealItems);
        mealList.setAdapter(mealAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("SavedMeals", MODE_PRIVATE);

        Set<String> savedMeals = sharedPreferences.getStringSet("savedMeals", null);
        if (savedMeals != null) {
            mealItems.addAll(savedMeals);
        }

        totalCarbIntake = sharedPreferences.getInt("savedCarbs", 0);

        SpannableString hint = new SpannableString("Use Title case with spaces to separate words.\n");
        hint.setSpan(new AbsoluteSizeSpan(14, true), 0, hint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        foodText.setHint(hint);


        mealAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mealItems);
        mealList.setAdapter(mealAdapter);

        Intent MealPlannerToOptions = new Intent(MealPlanner.this, Features.class);

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

        currentBloodGlucose.setText("Current BG: " + currentBG + " mg/dL");

        activityResultLauncherMP = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK) {
                            //
                        }
                    }
                });

        targetBGText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                try {
                    targetBG = Integer.parseInt(s.toString());
                }catch(Exception e){
                    Log.d("NumFormatErrorBG",""+e.toString());
                }
            }
        });

        insulinDoseText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                try {
                    insulinDose = Integer.parseInt(s.toString());
                }catch(Exception e){
                    Log.d("NumFormatErrorISD",""+e.toString());
                }
            }
        });

        insulinSensitivityRatioText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                try{
                    insulinSensitivityRatio = Integer.parseInt(s.toString());
                }catch(Exception e){
                    Log.d("NumFormatErrorISR",""+e.toString());
                }
            }
        });

        foodText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                food = s.toString();
            }
        });

        servingSizeText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String input = s.toString().trim();
                    if (input.isEmpty()) {
                        // Set default value or zero if empty
                        servingSize = 0;
                    } else {
                        servingSize = Double.parseDouble(input);
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid number format gracefully
                    servingSize = 0;
                    Toast.makeText(MealPlanner.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }
            }

        });


        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (food == null || food.trim().isEmpty()) {
                    Toast.makeText(MealPlanner.this, "Please enter a food name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String inputFood = food.toString().trim();

                if (!foodItemsList.containsKey(inputFood)) {
                    // Ask user directly for carb value for the food they entered
                    AlertDialog.Builder carbDialog = new AlertDialog.Builder(MealPlanner.this);
                    carbDialog.setTitle("New Food Item");
                    carbDialog.setMessage("Enter the carbs per serving for \"" + inputFood + "\":");

                    final EditText carbInput = new EditText(MealPlanner.this);
                    carbInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                    carbDialog.setView(carbInput);

                    carbDialog.setPositiveButton("Add", (dialog2, which2) -> {
                        String carbText = carbInput.getText().toString().trim();
                        if (!carbText.isEmpty()) {
                            try {
                                int carbValue = Integer.parseInt(carbText);
                                foodItemsList.put(inputFood, carbValue);

                                // Save to SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                StringBuilder builder = new StringBuilder();
                                for (Map.Entry<String, Integer> entry : foodItemsList.entrySet()) {
                                    builder.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
                                }
                                editor.putString("carbValues", builder.toString());
                                editor.apply();

                                Toast.makeText(MealPlanner.this, inputFood + " added with " + carbValue + "g carbs.", Toast.LENGTH_SHORT).show();

                                // Now proceed with adding the meal
                                Integer carbVal = foodItemsList.get(inputFood);
                                if (servingSize <= 0) {
                                    Toast.makeText(MealPlanner.this, "Please enter a valid serving size.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                double carbsForServing = carbVal * servingSize;
                                totalCarbIntake += carbsForServing;
                                double carbEffectFactor = 3.0 + (Math.random() * 2);
                                double projectedBG = currentBG - (insulinDose * insulinSensitivityRatio) + carbEffectFactor * totalCarbIntake;

                                if (projectedBG > targetBG) {
                                    Toast.makeText(MealPlanner.this, "Warning: Projected BG (" + (int) projectedBG + " mg/dL) is above your target!", Toast.LENGTH_LONG).show();
                                    totalCarbIntake -= carbsForServing;
                                    return;
                                }

                                mealItems.add(servingSize+" "+inputFood + " | " + carbsForServing + " g carbs");
                                mealAdapter.notifyDataSetChanged();
                                foodText.setText("");
                                servingSizeText.setText("");

                            } catch (NumberFormatException e) {
                                Toast.makeText(MealPlanner.this, "Invalid number entered.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    carbDialog.setNegativeButton("Cancel", null);
                    carbDialog.show();
                } else {
                    // Food is already in the list, proceed with adding
                    Integer carbVal = foodItemsList.get(inputFood);
                    if (servingSize <= 0) {
                        Toast.makeText(MealPlanner.this, "Please enter a valid serving size.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double carbsForServing = carbVal * servingSize;
                    totalCarbIntake += carbsForServing;
                    double carbEffectFactor = 3.0 + (Math.random() * 2);
                    double projectedBG = currentBG - (insulinDose * insulinSensitivityRatio) + carbEffectFactor * totalCarbIntake;

                    if (projectedBG > targetBG) {
                        Toast.makeText(MealPlanner.this, "Warning: Projected BG (" + (int) projectedBG + " mg/dL) is above your target!", Toast.LENGTH_LONG).show();
                        totalCarbIntake -= carbsForServing;
                        return;
                    }

                    mealItems.add(servingSize+" "+inputFood + " | " + carbsForServing + " g carbs");
                    mealAdapter.notifyDataSetChanged();
                    foodText.setText("");
                    servingSizeText.setText("");
                }
            }
        });

        backButtonMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncherMP.launch(MealPlannerToOptions);
            }
        });

        removeMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mealItems.isEmpty()) {
                    // Get the last meal item string
                    String lastItem = mealItems.get(mealItems.size() - 1);

                    // Expected format: "[servingSize] [foodName] | [carbsForServing] g carbs"
                    // Example: "1.5 Apple | 37.5 g carbs"

                    try {
                        // Split by " | " to separate food info and carb info
                        String[] parts = lastItem.split("\\|");
                        if (parts.length == 2) {
                            String carbsPart = parts[1].trim(); // e.g. "37.5 g carbs"
                            // Remove " g carbs" and parse the number
                            String carbValueStr = carbsPart.replace("g carbs", "").trim();
                            double carbValue = Double.parseDouble(carbValueStr);

                            // Subtract from totalCarbIntake
                            totalCarbIntake -= carbValue;

                            if (totalCarbIntake < 0) totalCarbIntake = 0; // safety check
                        }
                    } catch (Exception e) {
                        Toast.makeText(MealPlanner.this, "Error removing last meal carbs.", Toast.LENGTH_SHORT).show();
                        Log.e("RemoveMealError", e.toString());
                    }

                    // Remove the item and update the adapter
                    mealItems.remove(mealItems.size() - 1);
                    mealAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MealPlanner.this, "No meals to remove.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("SavedMeals", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Convert mealItems to a Set to store in SharedPreferences
                    Set<String> mealSet = new HashSet<>(mealItems);
                    editor.putStringSet("savedMeals", mealSet);

                    // Save total carbohydrate intake
                    editor.putInt("savedCarbs", totalCarbIntake);

                    // Commit the changes
                    editor.apply();

                    Toast.makeText(MealPlanner.this, "Meals saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}