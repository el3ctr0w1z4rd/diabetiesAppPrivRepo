package com.example.diabeticmealplannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.diabeticmealplannerapp.api.ApiClient;
import com.example.diabeticmealplannerapp.api.GroqApiService;
import com.example.diabeticmealplannerapp.api.GroqChatRequest;
import com.example.diabeticmealplannerapp.api.GroqChatResponse;
import com.example.diabeticmealplannerapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private TextView chatTextView;
    private EditText inputEditText;
    private Button sendButton;
    private Button backButton;
    private ScrollView chatScrollView;
    private ProgressBar progressBar;
    
    private List<GroqChatRequest.Message> conversationHistory;
    private GroqApiService groqApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeViews();
        setupApiService();
        initializeConversation();
        setupClickListeners();
    }

    private void initializeViews() {
        chatTextView = findViewById(R.id.chatTextView);
        chatTextView.setMovementMethod(new ScrollingMovementMethod());
        inputEditText = findViewById(R.id.inputEditText);
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton);
        chatScrollView = findViewById(R.id.chatScrollView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupApiService() {
        groqApiService = ApiClient.getGroqApiService();
    }

    private void initializeConversation() {
        conversationHistory = new ArrayList<>();
        
        // Add system message for diabetes-focused responses
        GroqChatRequest.Message systemMessage = new GroqChatRequest.Message("system", 
            "You are a helpful assistant specialized in diabetes management, nutrition, and blood glucose monitoring. " +
            "Provide accurate, helpful information about diabetes care, meal planning, carbohydrate counting, " +
            "and blood glucose management. Always remind users to consult healthcare professionals for medical advice.");
        
        conversationHistory.add(systemMessage);
        
        appendChat("Assistant: Hello! I'm here to help you with diabetes management, nutrition questions, and blood glucose monitoring. How can I assist you today?");
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> {
            String userMessage = inputEditText.getText().toString().trim();
            if (userMessage.isEmpty()) {
                Toast.makeText(this, "Please enter a question", Toast.LENGTH_SHORT).show();
                return;
            }
            sendMessage(userMessage);
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Features.class);
            startActivity(intent);
            finish();
        });
    }

    private void sendMessage(String userMessage) {
        appendChat("You: " + userMessage);
        inputEditText.setText("");
        
        // Add user message to conversation history
        conversationHistory.add(new GroqChatRequest.Message("user", userMessage));
        
        // Show loading state
        setLoadingState(true);
        
        // Send to Groq API
        sendMessageToGroq();
    }

    private void sendMessageToGroq() {
        if (BuildConfig.GROQ_API_KEY.isEmpty()) {
            appendChat("Assistant: API key not configured. Please add your Groq API key to use the chat feature.");
            setLoadingState(false);
            return;
        }

        GroqChatRequest request = new GroqChatRequest("llama-3.3-70b-versatile", conversationHistory);
        
        Call<GroqChatResponse> call = groqApiService.getChatCompletion(
            "Bearer " + BuildConfig.GROQ_API_KEY,
            "application/json",
            request
        );

        call.enqueue(new Callback<GroqChatResponse>() {
            @Override
            public void onResponse(Call<GroqChatResponse> call, Response<GroqChatResponse> response) {
                setLoadingState(false);
                
                if (response.isSuccessful() && response.body() != null && 
                    !response.body().getChoices().isEmpty()) {
                    
                    String reply = response.body().getChoices().get(0).getMessage().getContent();
                    
                    // Add assistant response to conversation history
                    conversationHistory.add(new GroqChatRequest.Message("assistant", reply));
                    
                    appendChat("Assistant: " + reply);
                } else {
                    appendChat("Assistant: Sorry, I encountered an error. Please try again.");
                    Toast.makeText(ChatActivity.this, "API Error: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GroqChatResponse> call, Throwable t) {
                setLoadingState(false);
                appendChat("Assistant: Sorry, I'm having trouble connecting. Please check your internet connection and try again.");
                Toast.makeText(ChatActivity.this, Constants.ERROR_NETWORK, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void appendChat(String message) {
        runOnUiThread(() -> {
            chatTextView.append(message + "\n\n");
            chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
        });
    }

    private void setLoadingState(boolean isLoading) {
        runOnUiThread(() -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            sendButton.setEnabled(!isLoading);
            sendButton.setText(isLoading ? "Sending..." : "Send");
        });
    }
}