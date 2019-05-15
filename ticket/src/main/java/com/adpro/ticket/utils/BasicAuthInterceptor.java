package com.adpro.ticket.utils;

import lombok.AllArgsConstructor;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;

@AllArgsConstructor
public class BasicAuthInterceptor implements Interceptor {
    private String username;
    private String password;

    @Override
    public Response intercept(Chain chain) throws IOException {
        var request = chain.request()
            .newBuilder()
            .addHeader("Authorization", Credentials.basic(username, password));
        return chain.proceed(request.build());
    }
}
