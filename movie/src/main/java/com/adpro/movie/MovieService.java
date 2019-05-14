package com.adpro.movie;

import java.util.List;

public interface MovieService {
    List<Movie> getTodayShowingMovies();

    List<Movie> getTodayUpcomingMovies();

    List<MovieSession> getTodayMovieSessions(Long movieId);

    Movie getMovie(Long movieId);

    MovieSession getMovieSession(Long movieSessionId);
}
