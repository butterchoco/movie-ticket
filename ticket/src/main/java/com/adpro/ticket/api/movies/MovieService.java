package com.adpro.ticket.api.movies;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MovieService {
    @POST("api/movie/session/{id}")
    CompletableFuture<MovieSession> getMovieSessionById(@Path("id") Long id);

    @POST("saving-booking")
    @FormUrlEncoded
    CompletableFuture<JsonNode> saveBooking(@Field("sessionId") Long sessionId,
                                            @Field("seatId") List<String> seatIds);
}