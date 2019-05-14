package com.adpro.ticket.configuration;

import com.adpro.ticket.api.movies.MovieService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class MoviesConfig {
    @Bean
    public MovieService getMovieService() {
        final String BASE_URL_STRING = "http://ap-c8-movie.herokuapp.com/api/";
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_STRING)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(MovieService.class);
    }
}
