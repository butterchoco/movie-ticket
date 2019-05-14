package com.adpro.ticket.services.email;

import com.adpro.ticket.api.notifications.MessageResponse;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.concurrent.CompletableFuture;

public interface EmailClient {
    @POST("messages")
    CompletableFuture<MessageResponse> sendEmail(@Body MultipartBody body);
}
