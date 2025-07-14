package com.example.diabeticmealplannerapp.models;

public class FoodItem {
    private String name;
    private int carbsPer100g;
    private int glycemicIndex;
    private double calories;
    
    public FoodItem(String name, int carbsPer100g, int glycemicIndex, double calories) {
        this.name = name;
        this.carbsPer100g = carbsPer100g;
        this.glycemicIndex = glycemicIndex;
        this.calories = calories;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getCarbsPer100g() { return carbsPer100g; }
    public void setCarbsPer100g(int carbsPer100g) { this.carbsPer100g = carbsPer100g; }
    
    public int getGlycemicIndex() { return glycemicIndex; }
    public void setGlycemicIndex(int glycemicIndex) { this.glycemicIndex = glycemicIndex; }
    
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    
    public double getCarbsForServing(double servingSize) {
        return (carbsPer100g * servingSize) / 100.0;
    }
    
    public double getCaloriesForServing(double servingSize) {
        return (calories * servingSize) / 100.0;
    }
}