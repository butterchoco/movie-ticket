package com.adpro.movie.tmdb;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Get TMDBMovie from TMDB API.
 */
public class TMDBRepository {
    private static final String KEY = "8d14316a3ad3955af1670a00e39e50ab";

    private TMDBClient tmdbClient;

    @Autowired
    public TMDBRepository(TMDBClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    /**
     * Get FullMovie from the individual movie API.
     * @param movieId the tmdb ID of the movie
     * @return the FullMovie object
     */
    @SneakyThrows
    public FullTMDBMovie getMovie(Long movieId) {
        FullTMDBMovie fullTMDBMovie = tmdbClient.movie(movieId, KEY).execute().body();
        if (fullTMDBMovie == null) {
            throw new RuntimeException("Got null from TMDB API");
        }
        return fullTMDBMovie;
    }

    @SneakyThrows
    public List<PartialTMDBMovie> getLastMovies() {
        String lastDate = LocalDate.now().minusDays(7).format(DateTimeFormatter.ISO_LOCAL_DATE);
        var params = Map.of(
                "primary_release_date.gte", lastDate,
                "with_original_language", "en"
        );
        Page<PartialTMDBMovie> movies = tmdbClient.discover(KEY, params).execute().body();
        if (movies == null) {
            throw new RuntimeException("Got null from TMDB API");
        }
        return movies.getResults();
    }
}
