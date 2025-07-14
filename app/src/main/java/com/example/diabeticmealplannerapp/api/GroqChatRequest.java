package com.example.diabeticmealplannerapp.api;

import java.util.List;

public class GroqChatRequest {
    private String model;
    private List<Message> messages;
    private double temperature;
    private int max_tokens;

    public GroqChatRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
        this.temperature = 0.7; // Good balance for diabetes advice
        this.max_tokens = 800;   // Optimized for free tier
    }

    // Getters and setters
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
    
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    
    public int getMax_tokens() { return max_tokens; }
    public void setMax_tokens(int max_tokens) { this.max_tokens = max_tokens; }

    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        // Getters and setters
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}