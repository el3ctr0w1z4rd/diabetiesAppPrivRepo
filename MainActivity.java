package com.example.diabeticmealplannerapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView appName;
    Button launchApp;
    Button termsOfService;
    ActivityResultLauncher<Intent> featuresListClassLauncher;

    boolean acceptedTOS = false;

    int numOfTOSButtonClicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appName = findViewById(R.id.appName);
        launchApp = findViewById(R.id.launchAppButton);
        termsOfService = findViewById(R.id.tosButton);

        launchApp.setEnabled(false); // initially disabled

        featuresListClassLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Optional: Handle returned results from Features activity
                    }
                }
        );

        launchApp.setOnClickListener(v -> {
            if (acceptedTOS) {
                try {
                    Intent featuresListIntent = new Intent(MainActivity.this, Features.class);
                    featuresListClassLauncher.launch(featuresListIntent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        termsOfService.setOnClickListener(v -> {
            if(numOfTOSButtonClicks == 0) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Terms of Service")
                        .setMessage("This app is catered towards diabetic individuals and helps with meal planning by tracking glycemic indexes, insulin levels, and blood glucose.")
                        .setCancelable(false)
                        .setPositiveButton("Accept", (dialog, which) -> {
                            launchApp.setEnabled(true);
                            acceptedTOS = true;
                        })
                        .setNegativeButton("Decline", (dialog, which) -> {
                            launchApp.setEnabled(false);
                            acceptedTOS = false;
                            numOfTOSButtonClicks = 0;
                        })
                        .show();
                numOfTOSButtonClicks++;
            }else{
                Toast.makeText(MainActivity.this,"You already accepted the app's TOS",Toast.LENGTH_LONG).show();
            }
        });
    }
}