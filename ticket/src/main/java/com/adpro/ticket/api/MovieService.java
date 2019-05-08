package com.adpro.ticket.api;

import com.adpro.movie.MovieSession;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MovieService {
    @POST("movie/session/{id}")
    Call<MovieSession> getMovieSessionById(@Path("id") Long id);
}