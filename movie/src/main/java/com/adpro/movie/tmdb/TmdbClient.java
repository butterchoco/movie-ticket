package com.adpro.movie.tmdb;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface TmdbClient {
    @GET("discover/movie")
    Call<Page<PartialTmdbMovie>> discover(@Query("api_key") String apiKey,
                                          @QueryMap Map<String, String> params);

    @GET("movie/{id}")
    Call<FullTmdbMovie> movie(@Path("id") Long id, @Query("api_key") String apiKey);
}