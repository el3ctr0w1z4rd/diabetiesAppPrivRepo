package com.example.diabeticmealplannerapp.utils;

public class ValidationUtils {
    
    public static boolean isValidBloodGlucose(int value) {
        return value > 0 && value <= 600; // Reasonable BG range
    }
    
    public static boolean isValidInsulinDose(double dose) {
        return dose >= 0 && dose <= 100; // Reasonable insulin dose range
    }
    
    public static boolean isValidServingSize(double size) {
        return size > 0 && size <= 10; // Reasonable serving size range
    }
    
    public static boolean isValidTimeAfterMeal(double hours) {
        return hours >= 0 && hours <= 24; // 24 hours max
    }
    
    public static boolean isValidTargetBG(int target) {
        return target >= 70 && target <= 200; // Reasonable target range
    }
    
    public static boolean isValidISF(int isf) {
        return isf > 0 && isf <= 200; // Reasonable ISF range
    }
    
    public static String validateFoodName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Food name cannot be empty";
        }
        if (name.length() > 50) {
            return "Food name is too long";
        }
        return null; // Valid
    }
}