package com.adpro.movie;

import java.util.List;
import org.springframework.data.util.Pair;

interface MovieService {
    List<Movie> getTodayShowingMovies();

    List<Movie> getTodayUpcomingMovies();

    List<MovieSession> getTodayMovieSessions(Long movieId);

    Movie getMovie(Long movieId);
}
