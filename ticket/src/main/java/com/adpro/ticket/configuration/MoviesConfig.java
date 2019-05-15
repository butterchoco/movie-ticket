package com.adpro.ticket.configuration;

import com.adpro.ticket.api.movies.MovieService;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.Duration;

@Configuration
public class MoviesConfig {
    @Bean
    public MovieService getMovieService() {
        final String BASE_URL_STRING = "http://ap-c8-movie.herokuapp.com/";
        final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(Duration.ofMinutes(1))
            .build();

        return new Retrofit.Builder()
            .baseUrl(BASE_URL_STRING)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(MovieService.class);
    }
}
