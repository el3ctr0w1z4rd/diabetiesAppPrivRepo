@@ .. @@
 package com.example.diabeticmealplannerapp;

 import android.app.AlertDialog;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.os.Bundle;
 import android.text.InputType;
 import android.widget.*;
 import androidx.activity.EdgeToEdge;
 import androidx.appcompat.app.AppCompatActivity;
+import com.example.diabeticmealplannerapp.models.BloodGlucoseReading;
+import com.example.diabeticmealplannerapp.utils.Constants;
+import com.example.diabeticmealplannerapp.utils.ValidationUtils;

 public class BGPredictor extends AppCompatActivity {

     private TextView bloodGlucoseText;
     private TextView bloodGlucoseStatus;
     private Button logBloodGlucose;
     private Button predictBloodGlucose;
     private Button backButtonBGP;

-    private int bloodGlucoseVal = -1;
+    private BloodGlucoseReading currentReading;
     private double manualInsulinUnits = -1;
-    private double timeAfterLastMeal = 0;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
@@ .. @@
         backButtonBGP = findViewById(R.id.backButtonGP);

         // Load saved data
-        SharedPreferences prefs = getSharedPreferences("bloodGlucoseData", MODE_PRIVATE);
-        bloodGlucoseVal = prefs.getInt("value", -1);
-        String savedStatus = prefs.getString("status", null);
-
-        if (bloodGlucoseVal != -1) {
-            bloodGlucoseText.setText("Current BG: " + bloodGlucoseVal + " mg/dL");
-        }
-
-        if (savedStatus != null) {
-            bloodGlucoseStatus.setText(savedStatus);
-        }
+        loadSavedData();

         // Logging new blood glucose
         logBloodGlucose.setOnClickListener(v -> showLogBGDialog());

         // Start prediction process
         predictBloodGlucose.setOnClickListener(v -> {
-            if (bloodGlucoseVal <= 0) {
+            if (currentReading == null || currentReading.getValue() <= 0) {
                 Toast.makeText(this, "Please log your blood glucose first!", Toast.LENGTH_SHORT).show();
             } else {
                 startStepByStepDialog();
@@ .. @@
         });
     }

+    private void loadSavedData() {
+        SharedPreferences prefs = getSharedPreferences(Constants.PREF_BLOOD_GLUCOSE, MODE_PRIVATE);
+        int savedValue = prefs.getInt("value", -1);
+        double savedTimeAfterMeal = prefs.getFloat("timeAfterMeal", 0);
+        
+        if (savedValue != -1) {
+            currentReading = new BloodGlucoseReading(savedValue, savedTimeAfterMeal);
+            updateUI();
+        }
+    }
+
+    private void updateUI() {
+        if (currentReading != null) {
+            bloodGlucoseText.setText("Current BG: " + currentReading.getValue() + " mg/dL");
+            bloodGlucoseStatus.setText(currentReading.getStatus());
+        }
+    }
+
     private void showLogBGDialog() {
         EditText input = new EditText(this);
         input.setHint("e.g., 150");
@@ .. @@
                 .setPositiveButton("Log", (dialog, which) -> {
                     try {
-                        bloodGlucoseVal = Integer.parseInt(input.getText().toString());
-                        bloodGlucoseText.setText("Current BG: " + bloodGlucoseVal + " mg/dL");
-                        saveBGData();
+                        int bgValue = Integer.parseInt(input.getText().toString());
+                        if (!ValidationUtils.isValidBloodGlucose(bgValue)) {
+                            Toast.makeText(this, "Please enter a valid blood glucose value (1-600)", Toast.LENGTH_SHORT).show();
+                            return;
+                        }
                         askTimeAfterMeal();
                     } catch (NumberFormatException e) {
-                        Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
+                        Toast.makeText(this, Constants.ERROR_INVALID_INPUT, Toast.LENGTH_SHORT).show();
                     }
                 })
@@ .. @@
                 .setPositiveButton("OK", (dialog, which) -> {
                     try {
-                        timeAfterLastMeal = Double.parseDouble(input.getText().toString());
-                        updateStatus();
+                        double timeAfterMeal = Double.parseDouble(input.getText().toString());
+                        if (!ValidationUtils.isValidTimeAfterMeal(timeAfterMeal)) {
+                            Toast.makeText(this, "Please enter a valid time (0-24 hours)", Toast.LENGTH_SHORT).show();
+                            return;
+                        }
+                        
+                        int bgValue = Integer.parseInt(input.getText().toString());
+                        currentReading = new BloodGlucoseReading(bgValue, timeAfterMeal);
+                        updateUI();
                         saveBGData();
                     } catch (NumberFormatException e) {
-                        Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
+                        Toast.makeText(this, Constants.ERROR_INVALID_INPUT, Toast.LENGTH_SHORT).show();
                     }
                 })
@@ .. @@
     }

-    private void updateStatus() {
-        String status;
-        if (bloodGlucoseVal <= 70) {
-            status = "Low Blood Glucose Level";
-        } else if (timeAfterLastMeal <= 2.0) {
-            status = (bloodGlucoseVal <= 180) ? "Healthy Blood Glucose Level" : "High Blood Glucose Level";
-        } else {
-            status = (bloodGlucoseVal < 150) ? "Healthy Blood Glucose Level" : "High Blood Glucose Level";
-        }
-        bloodGlucoseStatus.setText(status);
-    }
-
     private void saveBGData() {
-        SharedPreferences.Editor editor = getSharedPreferences("bloodGlucoseData", MODE_PRIVATE).edit();
-        editor.putInt("value", bloodGlucoseVal);
-        editor.putString("status", bloodGlucoseStatus.getText().toString());
+        if (currentReading == null) return;
+        
+        SharedPreferences.Editor editor = getSharedPreferences(Constants.PREF_BLOOD_GLUCOSE, MODE_PRIVATE).edit();
+        editor.putInt("value", currentReading.getValue());
+        editor.putFloat("timeAfterMeal", (float) currentReading.getTimeAfterMeal());
+        editor.putLong("timestamp", currentReading.getTimestamp());
         editor.apply();
     }

@@ .. @@
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
-            manualInsulinUnits = getValue(inputText);
-            if (manualInsulinUnits >= 0) {
+            double insulinUnits = getValue(inputText);
+            if (ValidationUtils.isValidInsulinDose(insulinUnits)) {
+                manualInsulinUnits = insulinUnits;
                 askISF(carbs);
                 dialog.dismiss();
+            } else {
+                Toast.makeText(this, "Please enter a valid insulin dose (0-100 units)", Toast.LENGTH_SHORT).show();
             }
         });
     }

@@ .. @@
                 .setCancelable(false)
                 .setPositiveButton("Calculate", (dialog, which) -> {
                     double isf = getValue(inputISF.getText().toString());
-                    if (isf >= 0) predictBG(carbs, isf);
+                    if (ValidationUtils.isValidISF((int) isf)) {
+                        predictBG(carbs, isf);
+                    } else {
+                        Toast.makeText(this, "Please enter a valid ISF (1-200)", Toast.LENGTH_SHORT).show();
+                    }
                 })
@@ .. @@
     }

     private void predictBG(double carbs, double isf) {
-        double carbFactor = 3 + Math.random() * 2;
+        double carbFactor = Constants.MIN_CARB_FACTOR + Math.random() * (Constants.MAX_CARB_FACTOR - Constants.MIN_CARB_FACTOR);
         double estimatedIncrease = carbs * carbFactor;
         double predictedDrop = manualInsulinUnits * isf;
-        double predictedBG = bloodGlucoseVal + estimatedIncrease - predictedDrop;
+        double predictedBG = currentReading.getValue() + estimatedIncrease - predictedDrop;

         new AlertDialog.Builder(this)
@@ .. @@
     private double getValue(String s) {
         try {
             return Double.parseDouble(s);
         } catch (NumberFormatException e) {
-            Toast.makeText(this, "Invalid input!", Toast.LENGTH_SHORT).show();
+            Toast.makeText(this, Constants.ERROR_INVALID_INPUT, Toast.LENGTH_SHORT).show();
             return -1;
         }
     }