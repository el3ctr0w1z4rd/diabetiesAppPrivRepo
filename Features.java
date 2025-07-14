package com.example.diabeticmealplannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Features extends AppCompatActivity {

    private ListView listOfFeatures;
    private final ArrayList<String> featuresList = new ArrayList<>();
    private ActivityResultLauncher<Intent> featuresClassLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features); // Make sure this matches your actual layout file name

        listOfFeatures = findViewById(R.id.listOfFeatures);

        // Fill feature list
        featuresList.add("Blood Glucose Tracker/Estimator");
        featuresList.add("Create Meal Plans");
        featuresList.add("Glycemic Index Reference");

        // Setup adapter
        FeaturesAdapter adapter = new FeaturesAdapter(this, R.layout.features_activity_layout, featuresList);
        listOfFeatures.setAdapter(adapter);

        // Register activity result launcher
        featuresClassLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            // Optional: handle result here
                        }
                    }
                }
        );

        // Define intents
        Intent bloodGlucosePredictorIntent = new Intent(this, BGPredictor.class);
        Intent mealPlannerIntent = new Intent(this, MealPlanner.class);
        Intent glycemicIndexReferenceIntent = new Intent(this, GlycemicIndexReference.class);
//        Intent homeIntent = new Intent(this, MainActivity.class);


        // List click listener
        listOfFeatures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        featuresClassLauncher.launch(bloodGlucosePredictorIntent);
                        break;
                    case 1:
                        featuresClassLauncher.launch(mealPlannerIntent);
                        break;
                    case 2:
                        featuresClassLauncher.launch(glycemicIndexReferenceIntent);
                        break;
                }
            }
        });
    }
}
