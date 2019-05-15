package com.adpro;

import com.adpro.movie.MovieListProxy;
import com.adpro.movie.MovieListRepository;
import com.adpro.movie.MovieRepository;
import com.adpro.movie.tmdb.TmdbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@ComponentScan
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

    @Bean
    @Primary
    @Autowired
    public MovieListRepository getMovieListRepository(MovieRepository movieRepository) {
        return new MovieListProxy(movieRepository);
    }
}
