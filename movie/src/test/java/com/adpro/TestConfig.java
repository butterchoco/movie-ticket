package com.adpro;

import static org.mockito.Mockito.mock;

import com.adpro.movie.Scheduler;
import com.adpro.movie.tmdb.TMDBRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan
public class TestConfig {

    /**
     * Don't hit TMDB API on test.
     * @return Mocked TMDBRepository.
     */
    @Bean
    @Primary
    TMDBRepository getTMDBRepository() {
        return mock(TMDBRepository.class);
    }

    @Bean
    @Primary
    Scheduler getScheduler() {
        return mock(Scheduler.class);
    }

}
