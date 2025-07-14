//package com.example.diabeticmealplannerapp;
//
//import android.os.Bundle;
//import android.text.method.ScrollingMovementMethod;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.function.Consumer;
//
//public class ChatActivity extends AppCompatActivity {
//    private TextView chatTextView;
//    private EditText inputEditText;
//    private Button sendButton;
//    private ScrollView chatScrollView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        chatTextView = findViewById(R.id.chatTextView);
//        chatTextView.setMovementMethod(new ScrollingMovementMethod());
//        inputEditText = findViewById(R.id.inputEditText);
//        sendButton = findViewById(R.id.sendButton);
//        chatScrollView = findViewById(R.id.chatScrollView);
//
//        sendButton.setOnClickListener(v -> {
//            String userMessage = inputEditText.getText().toString().trim();
//            if (userMessage.isEmpty()) {
//                Toast.makeText(this, "Please enter a question", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            appendChat("You: " + userMessage);
//            inputEditText.setText("");
//            sendMessageToGroq(userMessage);
//        });
//    }
//
//    private void appendChat(String message) {
//        chatTextView.append(message + "\n\n");
//        chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
//    }
//
//    private void sendMessageToGroq(String message) {
//        GroqChatUsage.sendMessageToGroq(message, new Consumer<String>() {
//            @Override
//            public void accept(String reply) {
//                runOnUiThread(() -> appendChat("Bot: " + reply));
//            }
//        }, new Consumer<String>() {
//            @Override
//            public void accept(String error) {
//                runOnUiThread(() -> Toast.makeText(ChatActivity.this, error, Toast.LENGTH_LONG).show());
//            }
//        });
//    }
//}
