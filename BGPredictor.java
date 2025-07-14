package com.example.diabeticmealplannerapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class BGPredictor extends AppCompatActivity {

    private TextView bloodGlucoseText;
    private TextView bloodGlucoseStatus;
    private Button logBloodGlucose;
    private Button predictBloodGlucose;
    private Button backButtonBGP;

    private int bloodGlucoseVal = -1;
    private double manualInsulinUnits = -1;
    private double timeAfterLastMeal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.blood_glucose_predictor);

        bloodGlucoseText = findViewById(R.id.bloodGlucose);
        bloodGlucoseStatus = findViewById(R.id.status);
        logBloodGlucose = findViewById(R.id.logBloodGlucose);
        predictBloodGlucose = findViewById(R.id.predictButton);
        backButtonBGP = findViewById(R.id.backButtonGP);

        // Load saved data
        SharedPreferences prefs = getSharedPreferences("bloodGlucoseData", MODE_PRIVATE);
        bloodGlucoseVal = prefs.getInt("value", -1);
        String savedStatus = prefs.getString("status", null);

        if (bloodGlucoseVal != -1) {
            bloodGlucoseText.setText("Current BG: " + bloodGlucoseVal + " mg/dL");
        }

        if (savedStatus != null) {
            bloodGlucoseStatus.setText(savedStatus);
        }

        // Logging new blood glucose
        logBloodGlucose.setOnClickListener(v -> showLogBGDialog());

        // Start prediction process
        predictBloodGlucose.setOnClickListener(v -> {
            if (bloodGlucoseVal <= 0) {
                Toast.makeText(this, "Please log your blood glucose first!", Toast.LENGTH_SHORT).show();
            } else {
                startStepByStepDialog();
            }
        });

        // Back button navigation
        backButtonBGP.setOnClickListener(v -> {
            Intent backIntent = new Intent(BGPredictor.this, Features.class);
            startActivity(backIntent);
        });
    }

    private void showLogBGDialog() {
        EditText input = new EditText(this);
        input.setHint("e.g., 150");
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(this)
                .setTitle("Log Current Blood Glucose")
                .setMessage("Enter your current blood glucose level (mg/dL):")
                .setView(input)
                .setPositiveButton("Log", (dialog, which) -> {
                    try {
                        bloodGlucoseVal = Integer.parseInt(input.getText().toString());
                        bloodGlucoseText.setText("Current BG: " + bloodGlucoseVal + " mg/dL");
                        saveBGData();
                        askTimeAfterMeal();
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void askTimeAfterMeal() {
        EditText input = new EditText(this);
        input.setHint("In hours (e.g., 0.5)");
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(this)
                .setTitle("Time Since Last Meal")
                .setMessage("Enter time since last meal (in hours):")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    try {
                        timeAfterLastMeal = Double.parseDouble(input.getText().toString());
                        updateStatus();
                        saveBGData();
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateStatus() {
        String status;
        if (bloodGlucoseVal <= 70) {
            status = "Low Blood Glucose Level";
        } else if (timeAfterLastMeal <= 2.0) {
            status = (bloodGlucoseVal <= 180) ? "Healthy Blood Glucose Level" : "High Blood Glucose Level";
        } else {
            status = (bloodGlucoseVal < 150) ? "Healthy Blood Glucose Level" : "High Blood Glucose Level";
        }
        bloodGlucoseStatus.setText(status);
    }

    private void saveBGData() {
        SharedPreferences.Editor editor = getSharedPreferences("bloodGlucoseData", MODE_PRIVATE).edit();
        editor.putInt("value", bloodGlucoseVal);
        editor.putString("status", bloodGlucoseStatus.getText().toString());
        editor.apply();
    }

    // ===== PREDICTION FLOW =====

    private void startStepByStepDialog() {
        manualInsulinUnits = -1;
        askCarbs();
    }

    private void askCarbs() {
        EditText inputCarbs = new EditText(this);
        inputCarbs.setHint("e.g., 60");
        inputCarbs.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(this)
                .setTitle("Step 1: Carbohydrate Intake")
                .setMessage("Enter the number of carbs in your upcoming meal (grams):")
                .setView(inputCarbs)
                .setCancelable(false)
                .setPositiveButton("Next", (dialog, which) -> {
                    double carbs = getValue(inputCarbs.getText().toString());
                    if (carbs >= 0) askManualInsulin(carbs);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void askManualInsulin(double carbs) {
        EditText inputInsulin = new EditText(this);
        inputInsulin.setHint("e.g., 6");
        inputInsulin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Step 2: Insulin Dose")
                .setMessage("Enter how many units of insulin you plan to take:")
                .setView(inputInsulin)
                .setCancelable(false)
                .setPositiveButton("Next", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String inputText = inputInsulin.getText().toString();
            if (inputText.isEmpty()) {
                Toast.makeText(this, "Please enter insulin units!", Toast.LENGTH_SHORT).show();
                return;
            }
            manualInsulinUnits = getValue(inputText);
            if (manualInsulinUnits >= 0) {
                askISF(carbs);
                dialog.dismiss();
            }
        });
    }

    private void askISF(double carbs) {
        EditText inputISF = new EditText(this);
        inputISF.setHint("e.g., 50");
        inputISF.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(this)
                .setTitle("Step 3: Insulin Sensitivity Factor (ISF)")
                .setMessage("How many mg/dL does 1 unit of insulin lower your BG?")
                .setView(inputISF)
                .setCancelable(false)
                .setPositiveButton("Calculate", (dialog, which) -> {
                    double isf = getValue(inputISF.getText().toString());
                    if (isf >= 0) predictBG(carbs, isf);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void predictBG(double carbs, double isf) {
        double carbFactor = 3 + Math.random() * 2;
        double estimatedIncrease = carbs * carbFactor;
        double predictedDrop = manualInsulinUnits * isf;
        double predictedBG = bloodGlucoseVal + estimatedIncrease - predictedDrop;

        new AlertDialog.Builder(this)
                .setTitle("Prediction Complete")
                .setMessage(
                        "Meal Carbs: " + carbs + "g\n" +
                                String.format("Carb Factor: %.2f mg/dL per gram\n", carbFactor) +
                                String.format("Estimated BG increase: %.1f mg/dL\n", estimatedIncrease) +
                                String.format("Insulin: %.2f units → BG drop: %.1f mg/dL\n", manualInsulinUnits, predictedDrop) +
                                "\n➡ Predicted BG: " + String.format("%.1f", predictedBG) + " mg/dL"
                )
                .setPositiveButton("OK", null)
                .show();
    }

    private double getValue(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }
}
