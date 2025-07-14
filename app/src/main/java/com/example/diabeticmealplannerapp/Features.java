@@ .. @@
         // Fill feature list
         featuresList.add("Blood Glucose Tracker/Estimator");
         featuresList.add("Create Meal Plans");
         featuresList.add("Glycemic Index Reference");
+        featuresList.add("AI Chat Assistant");
+        featuresList.add("Food Scanner");

         // Setup adapter
@@ .. @@
         Intent bloodGlucosePredictorIntent = new Intent(this, BGPredictor.class);
         Intent mealPlannerIntent = new Intent(this, MealPlanner.class);
         Intent glycemicIndexReferenceIntent = new Intent(this, GlycemicIndexReference.class);
-//        Intent homeIntent = new Intent(this, MainActivity.class);
+        Intent chatIntent = new Intent(this, ChatActivity.class);
+        Intent foodScannerIntent = new Intent(this, FoodScannerActivity.class);

         // List click listener
@@ .. @@
                     case 2:
                         featuresClassLauncher.launch(glycemicIndexReferenceIntent);
                         break;
+                    case 3:
+                        featuresClassLauncher.launch(chatIntent);
+                        break;
+                    case 4:
+                        featuresClassLauncher.launch(foodScannerIntent);
+                        break;
                 }
             }
         });