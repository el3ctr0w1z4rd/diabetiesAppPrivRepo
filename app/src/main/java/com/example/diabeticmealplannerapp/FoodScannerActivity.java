package com.example.diabeticmealplannerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.diabeticmealplannerapp.api.ApiClient;
import com.example.diabeticmealplannerapp.api.OpenAIApiService;
import com.example.diabeticmealplannerapp.api.OpenAIRequest;
import com.example.diabeticmealplannerapp.api.OpenAIResponse;
import com.example.diabeticmealplannerapp.models.FoodItem;
import com.example.diabeticmealplannerapp.utils.Constants;
import com.example.diabeticmealplannerapp.utils.FoodDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodScannerActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView resultTextView;
    private Button captureButton;
    private Button selectImageButton;
    private Button backButton;
    private ProgressBar progressBar;
    
    private OpenAIApiService openAIApiService;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_scanner);

        initializeViews();
        setupApiService();
        setupActivityLaunchers();
        setupClickListeners();
    }

    private void initializeViews() {
        imageView = findViewById(R.id.imageView);
        resultTextView = findViewById(R.id.resultTextView);
        captureButton = findViewById(R.id.captureButton);
        selectImageButton = findViewById(R.id.selectImageButton);
        backButton = findViewById(R.id.backButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupApiService() {
        openAIApiService = ApiClient.getOpenAIApiService();
    }

    private void setupActivityLaunchers() {
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);
                    analyzeFood(imageBitmap);
                }
            }
        );

        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        imageView.setImageBitmap(bitmap);
                        analyzeFood(bitmap);
                    } catch (IOException e) {
                        Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );

        permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    private void setupClickListeners() {
        captureButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Features.class);
            startActivity(intent);
            finish();
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    private void analyzeFood(Bitmap bitmap) {
        if (BuildConfig.OPENAI_API_KEY.isEmpty()) {
            resultTextView.setText("OpenAI API key not configured. Please add your API key to use food scanning.");
            return;
        }

        setLoadingState(true);
        
        // Convert bitmap to base64
        String base64Image = bitmapToBase64(bitmap);
        
        // Create vision request
        List<OpenAIRequest.Message> messages = new ArrayList<>();
        
        // Create content array for vision
        List<Map<String, Object>> content = new ArrayList<>();
        
        // Add text part
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("type", "text");
        textPart.put("text", "Analyze this food image and provide the following information in JSON format:\n" +
            "{\n" +
            "  \"food_name\": \"name of the food\",\n" +
            "  \"carbs_per_100g\": number,\n" +
            "  \"calories_per_100g\": number,\n" +
            "  \"glycemic_index\": number,\n" +
            "  \"confidence\": \"high/medium/low\",\n" +
            "  \"description\": \"brief description of what you see\"\n" +
            "}\n" +
            "If you cannot identify the food clearly, set confidence to 'low' and provide your best estimate.");
        content.add(textPart);
        
        // Add image part
        Map<String, Object> imagePart = new HashMap<>();
        imagePart.put("type", "image_url");
        Map<String, String> imageUrl = new HashMap<>();
        imageUrl.put("url", "data:image/jpeg;base64," + base64Image);
        imagePart.put("image_url", imageUrl);
        content.add(imagePart);
        
        OpenAIRequest.Message message = new OpenAIRequest.Message("user", content);
        messages.add(message);
        
        OpenAIRequest request = new OpenAIRequest("gpt-4o-mini", messages, 500);
        
        Call<OpenAIResponse> call = openAIApiService.analyzeFood(
            "Bearer " + BuildConfig.OPENAI_API_KEY,
            "application/json",
            request
        );

        call.enqueue(new Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                setLoadingState(false);
                
                if (response.isSuccessful() && response.body() != null && 
                    !response.body().getChoices().isEmpty()) {
                    
                    String result = response.body().getChoices().get(0).getMessage().getContent();
                    parseAndDisplayResult(result);
                } else {
                    resultTextView.setText("Error analyzing food. Please try again.");
                    Toast.makeText(FoodScannerActivity.this, "API Error: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                setLoadingState(false);
                resultTextView.setText("Network error. Please check your connection and try again.");
                Toast.makeText(FoodScannerActivity.this, Constants.ERROR_NETWORK, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String bitmapToBase64(Bitmap bitmap) {
        // Resize bitmap if too large
        int maxSize = 512;
        if (bitmap.getWidth() > maxSize || bitmap.getHeight() > maxSize) {
            float ratio = Math.min((float) maxSize / bitmap.getWidth(), (float) maxSize / bitmap.getHeight());
            int width = Math.round(ratio * bitmap.getWidth());
            int height = Math.round(ratio * bitmap.getHeight());
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void parseAndDisplayResult(String jsonResult) {
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
            
            // Add to food database if confidence is high
            if ("high".equals(confidence) || "medium".equals(confidence)) {
                FoodItem newFood = new FoodItem(foodName, carbs, glycemicIndex, calories);
                FoodDatabase.addFoodItem(foodName, newFood);
            }
            
            // Display results
            StringBuilder result = new StringBuilder();
            result.append("Food Identified: ").append(foodName).append("\n\n");
            result.append("Description: ").append(description).append("\n\n");
            result.append("Nutritional Information (per 100g):\n");
            result.append("• Carbohydrates: ").append(carbs).append("g\n");
            result.append("• Calories: ").append(String.format("%.0f", calories)).append("\n");
            result.append("• Glycemic Index: ").append(glycemicIndex).append("\n\n");
            result.append("Confidence: ").append(confidence.toUpperCase());
            
            if ("low".equals(confidence)) {
                result.append("\n\nNote: Low confidence detection. Please verify the information.");
            }
            
            resultTextView.setText(result.toString());
            
        } catch (Exception e) {
            resultTextView.setText("Error parsing results: " + jsonResult);
        }
    }

    private void setLoadingState(boolean isLoading) {
        runOnUiThread(() -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            captureButton.setEnabled(!isLoading);
            selectImageButton.setEnabled(!isLoading);
            
            if (isLoading) {
                resultTextView.setText("Analyzing food image...");
            }
        });
    }
}