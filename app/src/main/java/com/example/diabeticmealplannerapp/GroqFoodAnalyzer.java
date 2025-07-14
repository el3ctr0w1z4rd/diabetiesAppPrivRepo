package com.example.diabeticmealplannerapp;

import android.content.Context;
import android.widget.Toast;

import com.example.diabeticmealplannerapp.api.ApiClient;
import com.example.diabeticmealplannerapp.api.GroqApiService;
import com.example.diabeticmealplannerapp.api.GroqChatRequest;
import com.example.diabeticmealplannerapp.api.GroqChatResponse;
import com.example.diabeticmealplannerapp.models.FoodItem;
import com.example.diabeticmealplannerapp.utils.FoodDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroqFoodAnalyzer {
    
    public interface FoodAnalysisCallback {
        void onSuccess(FoodItem foodItem, String description, String confidence);
        void onError(String error);
    }
    
    private GroqApiService groqApiService;
    private Context context;
    
    public GroqFoodAnalyzer(Context context) {
        this.context = context;
        this.groqApiService = ApiClient.getGroqApiService();
    }
    
    public void analyzeFoodByName(String foodName, FoodAnalysisCallback callback) {
        List<GroqChatRequest.Message> messages = new ArrayList<>();
        
        // System message for food analysis
        GroqChatRequest.Message systemMessage = new GroqChatRequest.Message("system", 
            "You are a nutrition expert specializing in diabetes management. When given a food name, " +
            "provide nutritional information in this exact JSON format:\n" +
            "{\n" +
            "  \"food_name\": \"exact food name\",\n" +
            "  \"carbs_per_100g\": number,\n" +
            "  \"calories_per_100g\": number,\n" +
            "  \"glycemic_index\": number,\n" +
            "  \"confidence\": \"high/medium/low\",\n" +
            "  \"description\": \"brief nutritional description for diabetics\"\n" +
            "}\n" +
            "Provide accurate values. If uncertain, use 'medium' or 'low' confidence.");
        
        GroqChatRequest.Message userMessage = new GroqChatRequest.Message("user", 
            "Analyze this food for a diabetic person: " + foodName + 
            ". Provide nutritional information including carbs per 100g, calories per 100g, and glycemic index.");
        
        messages.add(systemMessage);
        messages.add(userMessage);
        
        GroqChatRequest request = new GroqChatRequest("llama-3.1-8b-instant", messages);
        request.setMax_tokens(400);
        request.setTemperature(0.3); // Lower temperature for more consistent JSON
        
        Call<GroqChatResponse> call = groqApiService.getChatCompletion(
            "Bearer " + BuildConfig.GROQ_API_KEY,
            "application/json",
            request
        );
        
        call.enqueue(new Callback<GroqChatResponse>() {
            @Override
            public void onResponse(Call<GroqChatResponse> call, Response<GroqChatResponse> response) {
                if (response.isSuccessful() && response.body() != null && 
                    !response.body().getChoices().isEmpty()) {
                    
                    String result = response.body().getChoices().get(0).getMessage().getContent();
                    parseAndReturnFoodData(result, callback);
                } else {
                    callback.onError("Failed to analyze food. Please try again.");
                }
            }
            
            @Override
            public void onFailure(Call<GroqChatResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void parseAndReturnFoodData(String jsonResult, FoodAnalysisCallback callback) {
        try {
            // Clean the JSON if it's wrapped in markdown
            String cleanJson = jsonResult.trim();
            if (cleanJson.startsWith("```json")) {
                cleanJson = cleanJson.substring(7);
            }
            if (cleanJson.endsWith("```")) {
                cleanJson = cleanJson.substring(0, cleanJson.length() - 3);
            }
            cleanJson = cleanJson.trim();
            
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(cleanJson, JsonObject.class);
            
            String foodName = jsonObject.get("food_name").getAsString();
            int carbs = jsonObject.get("carbs_per_100g").getAsInt();
            double calories = jsonObject.get("calories_per_100g").getAsDouble();
            int glycemicIndex = jsonObject.get("glycemic_index").getAsInt();
            String confidence = jsonObject.get("confidence").getAsString();
            String description = jsonObject.get("description").getAsString();
            
            // Create food item
            FoodItem foodItem = new FoodItem(foodName, carbs, glycemicIndex, calories);
            
            // Add to database if confidence is high or medium
            if ("high".equals(confidence) || "medium".equals(confidence)) {
                FoodDatabase.addFoodItem(foodName, foodItem);
            }
            
            callback.onSuccess(foodItem, description, confidence);
            
        } catch (Exception e) {
            callback.onError("Error parsing food data: " + e.getMessage());
        }
    }
}