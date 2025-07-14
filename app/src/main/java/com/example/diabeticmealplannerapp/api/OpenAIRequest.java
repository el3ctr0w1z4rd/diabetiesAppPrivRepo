package com.example.diabeticmealplannerapp.api;

import java.util.List;

public class OpenAIRequest {
    private String model;
    private List<Message> messages;
    private int max_tokens;

    public OpenAIRequest(String model, List<Message> messages, int max_tokens) {
        this.model = model;
        this.messages = messages;
        this.max_tokens = max_tokens;
    }

    // Getters and setters
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
    
    public int getMax_tokens() { return max_tokens; }
    public void setMax_tokens(int max_tokens) { this.max_tokens = max_tokens; }

    public static class Message {
        private String role;
        private Object content; // Can be string or array for vision

        public Message(String role, Object content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        public Object getContent() { return content; }
        public void setContent(Object content) { this.content = content; }
    }
}