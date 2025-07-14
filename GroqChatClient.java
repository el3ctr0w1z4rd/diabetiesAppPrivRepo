//package com.example.diabeticmealplannerapp;
//
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class GroqChatClient {
//    private static final String BASE_URL = "https://api.groq.com/";
//
//    private static final GroqChatService SERVICE = new Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(GroqChatService.class);
//
//    public static GroqChatService getService() {
//        return SERVICE;
//    }
//}
