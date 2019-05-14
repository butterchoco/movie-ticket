package com.adpro.movie.tmdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.adpro.TestConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.mock.Calls;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class TmdbRepositoryTest {

    @MockBean
    private TmdbClient tmdbClient;

    private TmdbRepository tmdbRepository;

    private ObjectMapper mapper;

    @Before
    public void init() {
        mapper = new ObjectMapper();
        tmdbRepository = new TmdbRepository(tmdbClient);
    }

    @Test
    public void givenValidMovieId_thenDontThrowException() throws Exception {
        FullTmdbMovie fullTmdbMovie = mapper.readValue("{\"id\": 1," +
                "\"original_title\": \"Fairuzi Adventures\"," +
                "\"runtime\": 120}", FullTmdbMovie.class);
        Call<FullTmdbMovie> fullTMDBMovieResponse = Calls.response(fullTmdbMovie);
        given(tmdbClient.movie(any(), any()))
                .willReturn(fullTMDBMovieResponse);

        try {
            tmdbRepository.getMovie(1L);
        } catch (RuntimeException e) {
            fail("Should not throw Exception");
        }
    }

    @Test
    public void givenInvalidMovieId_thenThrowException() {
        Call<FullTmdbMovie> nullResponse = Calls.response(Response.success(null));
        given(tmdbClient.movie(eq(1L), any()))
                .willReturn(nullResponse);

        try {
            tmdbRepository.getMovie(1L);
            fail("Should throw Exception");
        } catch (RuntimeException e) {}
    }

    @Test
    public void givenNoRuntimeMovie_thenSetDefault() throws Exception {
        FullTmdbMovie fullTmdbMovie = mapper.readValue("{\"id\": 1," +
                "\"original_title\": \"Fairuzi Adventures\"," +
                "\"runtime\": null, \"poster_path\": \"fairuzi.jpg\"}", FullTmdbMovie.class);
        Call<FullTmdbMovie> fullTmdbMovieResponse = Calls.response(fullTmdbMovie);
        given(tmdbClient.movie(any(), any()))
                .willReturn(fullTmdbMovieResponse);

        FullTmdbMovie movie = tmdbRepository.getMovie(1L);
        assertEquals(FullTmdbMovie.DEFAULT_DURATION, movie.getDuration());
    }

    @Test
    public void givenValidMovieListResponse_thenDontThrowException() throws Exception {
        Page<PartialTmdbMovie> validMovieListJSON = mapper.readValue("{\"page\": 1," +
                "\"total_results\": 2," +
                "\"total_pages\": 1," +
                "\"results\": [" +
                "{\"original_title\": \"Fairuzi Adventures\"," +
                "\"runtime\": 120}," +
                "{\"original_title\": \"[BUKAN] Fairuzi Adventures\"," +
                "\"runtime\": 121}" +
                "]}", new TypeReference<Page<PartialTmdbMovie>>(){});
        Call<Page<PartialTmdbMovie>> validMoveListResponse = Calls.response(validMovieListJSON);
        given(tmdbClient.discover(any(), any()))
                .willReturn(validMoveListResponse);

        try {
            tmdbRepository.getLastMovies();
        } catch (RuntimeException e) {
            fail("Should not throw Exception");
        }
    }

    @Test
    public void givenNullMovieListResponse_thenThrowException() {
        Call<Page<PartialTmdbMovie>> nullResponse = Calls.response(Response.success(null));
        given(tmdbClient.discover(any(), any()))
                .willReturn(nullResponse);

        try {
            tmdbRepository.getLastMovies();
            fail("Should throw Exception");
        } catch (RuntimeException e) {}
    }

    @Test
    public void givenNoPosterPathMovie_thenSetNoPosterUrl() throws Exception {
        FullTmdbMovie fullTMDBMovie = mapper.readValue("{\"id\": 1," +
                "\"original_title\": \"Fairuzi Adventures\"," +
                "\"poster_path\": null}", FullTmdbMovie.class);
        Call<FullTmdbMovie> fullTMDBMovieResponse = Calls.response(fullTMDBMovie);
        given(tmdbClient.movie(any(), any()))
                .willReturn(fullTMDBMovieResponse);

        FullTmdbMovie movie = tmdbRepository.getMovie(1L);
        assertEquals(FullTmdbMovie.NO_POSTER_URL, movie.getPosterPath());
    }
}