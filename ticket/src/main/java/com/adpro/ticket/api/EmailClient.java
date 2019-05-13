package com.adpro.ticket.api;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.concurrent.CompletableFuture;

public interface EmailClient {
    @POST("messages")
    CompletableFuture<MessageResponse> sendEmail(@Body MultipartBody body);
}
