package com.example.diabeticmealplannerapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GroqApiService {
    @POST("openai/v1/chat/completions")
    Call<GroqChatResponse> getChatCompletion(
            @Header("Authorization") String authorization,
            @Header("Content-Type") String contentType,
            @Body GroqChatRequest request
    );
}