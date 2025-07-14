package com.example.diabeticmealplannerapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OpenAIApiService {
    @POST("v1/chat/completions")
    Call<OpenAIResponse> analyzeFood(
            @Header("Authorization") String authorization,
            @Header("Content-Type") String contentType,
            @Body OpenAIRequest request
    );
}