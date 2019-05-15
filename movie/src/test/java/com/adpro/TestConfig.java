package com.adpro;

import static org.mockito.Mockito.mock;

import com.adpro.movie.Scheduler;
import com.adpro.movie.tmdb.TmdbRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan
public class TestConfig {

    /**
     * Don't hit TMDB API on test.
     * @return Mocked TMDBRepository.
     */
    @Bean
    @Primary
    TmdbRepository getTmdbRepository() {
        return mock(TmdbRepository.class);
    }

    @Bean
    @Primary
    Scheduler getScheduler() {
        return mock(Scheduler.class);
    }

}
