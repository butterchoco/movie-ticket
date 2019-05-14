package com.adpro.ticket.api.movies;

import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.concurrent.CompletableFuture;

public interface MovieService {
    @POST("movie/session/{id}")
    CompletableFuture<MovieSession> getMovieSessionById(@Path("id") Long id);
}