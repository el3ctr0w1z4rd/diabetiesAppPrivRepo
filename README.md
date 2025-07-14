# Diabetic Meal Planner App

An Android application designed to help diabetic individuals manage their blood glucose levels, plan meals, and make informed dietary decisions.

## Features

### 1. Blood Glucose Tracker/Predictor
- Log current blood glucose readings
- Track time since last meal
- Predict future blood glucose levels based on:
  - Carbohydrate intake
  - Insulin dosage
  - Insulin sensitivity factor (ISF)
- Automatic status classification (Low/Healthy/High)

### 2. Meal Planner
- Plan meals with carbohydrate tracking
- Set target blood glucose levels
- Calculate projected blood glucose after meals
- Save and load meal plans
- Extensive food database with nutritional information

### 3. Glycemic Index Reference
- Comprehensive database of foods with:
  - Carbohydrate content per 100g
  - Glycemic index values
  - Calorie information
- Easy-to-browse list format

### 4. AI Chat Assistant
- Powered by Groq API
- Specialized in diabetes management advice
- Provides information about:
  - Nutrition and meal planning
  - Blood glucose management
  - Carbohydrate counting
  - General diabetes care

### 5. Food Scanner
- Uses OpenAI Vision API to analyze food images
- Automatically identifies food items
- Provides nutritional information:
  - Carbohydrates per 100g
  - Calories per 100g
  - Glycemic index
- Confidence rating for accuracy
- Adds new foods to the database

## Setup Instructions

### Prerequisites
- Android Studio
- Android SDK (API level 24 or higher)
- Groq API key (for chat functionality)
- OpenAI API key (for food scanning)

### Installation

1. Clone or download the project
2. Open in Android Studio
3. Add your API keys to `gradle.properties`:
   ```
   GROQ_API_KEY=your_groq_api_key_here
   OPENAI_API_KEY=your_openai_api_key_here
   ```

### Getting API Keys

#### Groq API Key
1. Visit [Groq Console](https://console.groq.com/)
2. Sign up for an account
3. Navigate to API Keys section
4. Create a new API key
5. Copy the key to your `gradle.properties` file

#### OpenAI API Key
1. Visit [OpenAI Platform](https://platform.openai.com/)
2. Sign up for an account
3. Navigate to API Keys section
4. Create a new API key
5. Copy the key to your `gradle.properties` file

### Build and Run
1. Sync the project with Gradle files
2. Build the project
3. Run on an Android device or emulator

## Architecture

### Key Components

- **Models**: Data structures for food items and blood glucose readings
- **API Services**: Retrofit interfaces for Groq and OpenAI APIs
- **Utils**: Helper classes for validation, constants, and food database
- **Activities**: Main UI components for each feature

### Data Storage
- SharedPreferences for persistent data storage
- In-memory food database with extensibility for new items
- Conversation history management for chat functionality

### Error Handling
- Input validation for all user inputs
- Network error handling with user-friendly messages
- API error handling with fallback responses

## Usage Tips

### Blood Glucose Tracking
1. Always log your current blood glucose before using prediction features
2. Enter accurate time since last meal for better status classification
3. Use consistent units (mg/dL for BG, units for insulin)

### Meal Planning
1. Set realistic target blood glucose levels
2. Enter accurate serving sizes for better carb calculations
3. Save successful meal plans for future reference

### Food Scanner
1. Take clear, well-lit photos of food items
2. Ensure the food is the main subject in the image
3. Verify results, especially for low-confidence detections

### Chat Assistant
1. Ask specific questions about diabetes management
2. Always consult healthcare professionals for medical decisions
3. Use the chat for educational purposes and general guidance

## Important Disclaimers

- This app is for educational and informational purposes only
- Always consult with healthcare professionals for medical advice
- Blood glucose predictions are estimates and should not replace actual testing
- Food scanning results should be verified for accuracy
- Individual responses to food and insulin may vary

## Technical Requirements

- Android 7.0 (API level 24) or higher
- Internet connection for AI features
- Camera permission for food scanning
- Storage permission for image selection

## Support

For technical issues or questions about the app, please refer to the code documentation or create an issue in the project repository.