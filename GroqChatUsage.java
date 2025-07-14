//package com.example.diabeticmealplannerapp;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//public class GroqChatUsage {
//    // Uses BuildConfig.GROQ_API_KEY for authorization
//    public static void sendMessageToGroq(String prompt, Consumer<String> onReply, Consumer<String> onError) {
//        GroqChatRequest.Message message = new GroqChatRequest.Message("user", prompt);
//        GroqChatRequest request = new GroqChatRequest("llama-3.3-70b-versatile", List.of(message));
//
//        GroqChatClient.getService().getChatCompletion("Bearer " + BuildConfig.GROQ_API_KEY, request)
//                .enqueue(new Callback<GroqChatResponse>() {
//                    @Override
//                    public void onResponse(Call<GroqChatResponse> call, Response<GroqChatResponse> response) {
//                        if (response.isSuccessful() && response.body() != null && !response.body().choices.isEmpty()) {
//                            String reply = response.body().choices.get(0).message.content;
//                            onReply.accept(reply);
//                        } else {
//                            onError.accept("API Error or empty response");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<GroqChatResponse> call, Throwable t) {
//                        onError.accept("Network failure: " + t.getMessage());
//                    }
//                });
//    }
//}
