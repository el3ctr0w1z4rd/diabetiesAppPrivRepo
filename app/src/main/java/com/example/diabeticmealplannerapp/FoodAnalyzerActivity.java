package com.example.diabeticmealplannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diabeticmealplannerapp.models.FoodItem;

public class FoodAnalyzerActivity extends AppCompatActivity {
    private EditText foodNameInput;
    private Button analyzeButton;
    private Button backButton;
    private TextView resultTextView;
    private ScrollView resultScrollView;
    private ProgressBar progressBar;
    
    private GroqFoodAnalyzer foodAnalyzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_analyzer);

        initializeViews();
        setupFoodAnalyzer();
        setupClickListeners();
    }

    private void initializeViews() {
        foodNameInput = findViewById(R.id.foodNameInput);
        analyzeButton = findViewById(R.id.analyzeButton);
        backButton = findViewById(R.id.backButton);
        resultTextView = findViewById(R.id.resultTextView);
        resultScrollView = findViewById(R.id.resultScrollView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupFoodAnalyzer() {
        foodAnalyzer = new GroqFoodAnalyzer(this);
    }

    private void setupClickListeners() {
        analyzeButton.setOnClickListener(v -> {
            String foodName = foodNameInput.getText().toString().trim();
            if (foodName.isEmpty()) {
                Toast.makeText(this, "Please enter a food name", Toast.LENGTH_SHORT).show();
                return;
            }
            analyzeFood(foodName);
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Features.class);
            startActivity(intent);
            finish();
        });
    }

    private void analyzeFood(String foodName) {
        setLoadingState(true);
        
        foodAnalyzer.analyzeFoodByName(foodName, new GroqFoodAnalyzer.FoodAnalysisCallback() {
            @Override
            public void onSuccess(FoodItem foodItem, String description, String confidence) {
                setLoadingState(false);
                displayResults(foodItem, description, confidence);
            }

            @Override
            public void onError(String error) {
                setLoadingState(false);
                resultTextView.setText("Error: " + error);
                Toast.makeText(FoodAnalyzerActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayResults(FoodItem foodItem, String description, String confidence) {
        StringBuilder result = new StringBuilder();
        result.append("🍎 FOOD ANALYSIS RESULTS\n");
        result.append("═══════════════════════\n\n");
        
        result.append("Food: ").append(foodItem.getName()).append("\n\n");
        
        result.append("📊 NUTRITIONAL INFO (per 100g):\n");
        result.append("• Carbohydrates: ").append(foodItem.getCarbsPer100g()).append("g\n");
        result.append("• Calories: ").append(String.format("%.0f", foodItem.getCalories())).append("\n");
        result.append("• Glycemic Index: ").append(foodItem.getGlycemicIndex()).append("\n\n");
        
        result.append("🩺 DIABETES IMPACT:\n");
        result.append(description).append("\n\n");
        
        result.append("🎯 CONFIDENCE: ").append(confidence.toUpperCase()).append("\n");
        
        if ("low".equals(confidence)) {
            result.append("\n⚠️ Note: Low confidence result. Please verify information.");
        }
        
        result.append("\n\n💡 This food has been added to your meal planner database!");
        
        resultTextView.setText(result.toString());
        resultScrollView.setVisibility(View.VISIBLE);
    }

    private void setLoadingState(boolean isLoading) {
        runOnUiThread(() -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            analyzeButton.setEnabled(!isLoading);
            analyzeButton.setText(isLoading ? "Analyzing..." : "Analyze Food");
            
            if (isLoading) {
                resultTextView.setText("🔍 Analyzing food with AI...\nPlease wait...");
                resultScrollView.setVisibility(View.VISIBLE);
            }
        });
    }
}