package com.adpro.ticket;

import com.adpro.ticket.api.MovieService;
import org.springframework.context.annotation.Bean;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class MainConfig {
    @Bean
    public MovieService getMovieService() {
        final String BASE_URL_STRING = "http://ap-c8-movie.herokuapp.com/";
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_STRING)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(MovieService.class);
    }
}
