package com.adpro;

import com.adpro.movie.tmdb.TmdbClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class MainConfig {

    @Bean
    public TmdbClient getTmdbClient() {
        final String base_url_string = "https://api.themoviedb.org/3/";
        return new Retrofit.Builder()
                .baseUrl(base_url_string)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(TmdbClient.class);
    }
}
