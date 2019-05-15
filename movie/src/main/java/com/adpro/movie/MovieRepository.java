package com.adpro.movie;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MovieRepository extends MovieListRepository,
        PagingAndSortingRepository<Movie, Long> {
    Movie findMovieById(Long movieId);

    List<Movie> findMoviesByReleaseDateBetween(
            LocalDate after, LocalDate before);

    List<Movie> findMoviesByReleaseDateAfter(LocalDate date);

    List<Movie> findByTmdbIdIn(List<Long> tmdbIds);
}
