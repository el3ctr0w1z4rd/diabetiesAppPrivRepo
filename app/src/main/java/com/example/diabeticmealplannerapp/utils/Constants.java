package com.example.diabeticmealplannerapp.utils;

public class Constants {
    // Blood Glucose Ranges
    public static final int LOW_BG_THRESHOLD = 70;
    public static final int HIGH_BG_THRESHOLD_AFTER_MEAL = 180;
    public static final int HIGH_BG_THRESHOLD_FASTING = 150;
    public static final double MEAL_TIME_THRESHOLD = 2.0; // hours
    
    // Carb calculation constants
    public static final double MIN_CARB_FACTOR = 3.0;
    public static final double MAX_CARB_FACTOR = 5.0;
    
    // API endpoints
    public static final String GROQ_BASE_URL = "https://api.groq.com/";
    public static final String OPENAI_BASE_URL = "https://api.openai.com/";
    
    // Groq model configurations for free tier
    public static final String GROQ_FREE_MODEL = "llama-3.1-8b-instant";
    public static final int GROQ_MAX_TOKENS = 800;
    public static final double GROQ_TEMPERATURE = 0.7;
    
    // SharedPreferences keys
    public static final String PREF_BLOOD_GLUCOSE = "bloodGlucoseData";
    public static final String PREF_SAVED_MEALS = "SavedMeals";
    public static final String PREF_FOOD_DATABASE = "FoodDatabase";
    
    // Error messages
    public static final String ERROR_INVALID_INPUT = "Invalid input. Please enter a valid number.";
    public static final String ERROR_NETWORK = "Network error. Please check your connection.";
    public static final String ERROR_API = "API error. Please try again later.";
}