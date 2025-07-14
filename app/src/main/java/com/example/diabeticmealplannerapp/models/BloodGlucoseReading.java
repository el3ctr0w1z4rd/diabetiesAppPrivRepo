package com.example.diabeticmealplannerapp.models;

public class BloodGlucoseReading {
    private int value;
    private long timestamp;
    private double timeAfterMeal;
    private String status;
    
    public BloodGlucoseReading(int value, double timeAfterMeal) {
        this.value = value;
        this.timeAfterMeal = timeAfterMeal;
        this.timestamp = System.currentTimeMillis();
        this.status = calculateStatus();
    }
    
    private String calculateStatus() {
        if (value <= 70) {
            return "Low Blood Glucose Level";
        } else if (timeAfterMeal <= 2.0) {
            return (value <= 180) ? "Healthy Blood Glucose Level" : "High Blood Glucose Level";
        } else {
            return (value < 150) ? "Healthy Blood Glucose Level" : "High Blood Glucose Level";
        }
    }
    
    // Getters and setters
    public int getValue() { return value; }
    public void setValue(int value) { 
        this.value = value; 
        this.status = calculateStatus();
    }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public double getTimeAfterMeal() { return timeAfterMeal; }
    public void setTimeAfterMeal(double timeAfterMeal) { 
        this.timeAfterMeal = timeAfterMeal;
        this.status = calculateStatus();
    }
    
    public String getStatus() { return status; }
}