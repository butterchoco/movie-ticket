package com.adpro;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.adpro.movie.Movie;
import com.adpro.movie.MovieListProxy;
import com.adpro.movie.MovieListRepository;
import com.adpro.movie.MovieRepository;
import com.adpro.movie.MovieService;
import com.adpro.movie.MovieSession;
import com.adpro.movie.MovieSessionRepository;
import com.adpro.seat.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestConfig.class })
@AutoConfigureMockMvc
public class MovieApplicationTest {

	@Autowired private MockMvc mvc;

	@Autowired private TheatreRepository theatreRepository;
	@Autowired private MovieService movieService;
	@Autowired private MovieRepository movieRepository;
	@Autowired private MovieSessionRepository movieSessionRepository;
	@Autowired private MovieListRepository movieListRepository;
	@MockBean private SavedBookingRepository savedBookingRepository;

	@Test
	public void contextLoads() {}

	@Before
	public void init() {
		movieSessionRepository.deleteAll();
		theatreRepository.deleteAll();
		movieRepository.deleteAll();
	}

	@Test
	public void testGetShowingMovies() throws Exception {
		Movie movie = Movie.builder()
				.name("Fairuzi Adventures")
				.description("Petualangan seorang Fairuzi")
				.duration(Duration.ofMinutes(111))
				.posterUrl("sdada")
				.releaseDate(LocalDate.now().minusDays(1))
				.tmdbId(1L)
				.build();
		movie = movieRepository.save(movie);
		System.out.println(movieRepository.findMoviesByReleaseDateBetween(
				LocalDate.now().minusDays(MovieListProxy.DAYS_SHOWED), LocalDate.now()) + " showing");
		System.out.println(movieService.getTodayShowingMovies() + " showing");

		this.mvc.perform(get("/api/movies/showing"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(movie.getId().intValue())))
				.andExpect(jsonPath("$[0].description", is(movie.getDescription())))
				.andExpect(jsonPath("$[0].posterUrl", is(movie.getPosterUrl())))
				.andExpect(jsonPath("$[0].releaseDate", is(movie.getReleaseDate().toString())))
				.andExpect(jsonPath("$[0].duration", is("01:51:00")));
	}

	@Test
	public void testGetUpcomingMovies() throws Exception {
		Movie movie = Movie.builder()
				.name("Fairuzi Adventures")
				.description("Petualangan seorang Fairuzi")
				.duration(Duration.ofMinutes(111))
				.posterUrl("sdada")
				.releaseDate(LocalDate.now().plusDays(7))
				.tmdbId(1L)
				.build();
		movie = movieRepository.save(movie);

		this.mvc.perform(get("/api/movies/upcoming"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(movie.getId().intValue())))
				.andExpect(jsonPath("$[0].description", is(movie.getDescription())))
				.andExpect(jsonPath("$[0].posterUrl", is(movie.getPosterUrl())))
				.andExpect(jsonPath("$[0].releaseDate", is(movie.getReleaseDate().toString())))
				.andExpect(jsonPath("$[0].duration", is("01:51:00")));
	}

	@Test
	public void testRootRedirectToMovies() throws Exception {
		this.mvc.perform(get("/"))
				.andExpect(redirectedUrl("/movies/showing"));
	}

	@Test
	public void testGetMovieSessions() throws Exception {
		Movie movie = Movie.builder()
				.name("Fairuzi Adventures")
				.description("Petualangan seorang Fairuzi")
				.duration(Duration.ofMinutes(111))
				.posterUrl("sdada")
				.releaseDate(LocalDate.now())
				.tmdbId(1L)
				.build();
		movie = movieRepository.save(movie);

		LocalDateTime dayTime = LocalDateTime.now().withHour(10);
		LocalDateTime nightTime = LocalDateTime.now().withHour(20);
		Theatre theatre = theatreRepository.save(new Theatre("A", 50));

		MovieSession daySession = movieSessionRepository.save(new MovieSession(movie, dayTime, theatre));
		MovieSession nightSession = movieSessionRepository.save(new MovieSession(movie, nightTime, theatre));
		movieSessionRepository.saveAll(List.of(daySession, nightSession));

		mvc.perform(get("/api/movie/" + movie.getId()))
				.andExpect(status().isOk());
	}

    @Test
    public void createTheatreAndSeat() {
        Theatre theatre1 = new Theatre("A", 50);
        Seat seat = new MiddleSeat();
        theatre1.addSeatToRow(seat);
    }

	@Test
	public void testSetSeatCost() {
		FarSeat.setCost(FarSeat.getCost() + 10000);
		MiddleSeat.setCost(MiddleSeat.getCost() + 10000);
	}

	@Test
	public void testBookingSeatAvailable() {
		Theatre theatre1 = new Theatre("A", 50);
		Seat seat = new MiddleSeat();
		theatre1.addSeatToRow(seat);
	}

	@Test
    public void synchronizeAPIWithTheatreAndSeat() throws Exception {
        Theatre theatre1 = theatreRepository.save(new Theatre("CGV", 50));
        theatre1.createRows();

        this.mvc.perform(get("/seat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(theatre1.getId())))
                .andExpect(jsonPath("$[0].description", is(theatre1.getDescription())))
                .andExpect(jsonPath("$[0].seatCount", is(theatre1.getSeatCount())))
                .andExpect(jsonPath("$[0].rows[0].type", is(theatre1.getRows().get(0).getType())));
    }

    @Test
    public void ShowingSeat() throws Exception {
		Theatre theatre1 = new Theatre("CGV", 50);
		theatre1.setId(1);
		theatre1.createRows();
	}

	@Test
	public void testRedirectToMovies() throws Exception {
		long duration = 111;
		Movie movie = Movie.builder()
				.name("Fairuzi Adventures")
				.description("Petualangan seorang Fairuzi")
				.duration(Duration.ofMinutes(duration))
				.posterUrl("sdada")
				.releaseDate(LocalDate.now())
				.tmdbId(1L)
				.build();
		movie = movieRepository.save(movie);

		LocalDateTime dayTime = LocalDateTime.now().withHour(10);
		Theatre theatre = theatreRepository.save(new Theatre("A", 50));
		MovieSession daySession = movieSessionRepository.save(new MovieSession(movie, dayTime, theatre));

        this.mvc.perform(get("/showing-seat/" + daySession.getId()))
                .andExpect(status().isOk());
    }

	@Test
	public void ShowingMoviesHtml() throws Exception {
		Movie movie = Movie.builder()
				.name("Fairuzi Adventures")
				.description("Petualangan seorang Fairuzi")
				.duration(Duration.ofMinutes(111))
				.posterUrl("sdada")
				.releaseDate(LocalDate.now())
				.tmdbId(1L)
				.build();
		movie = movieRepository.save(movie);

		this.mvc.perform(get("/movies/showing"))
				.andExpect(status().isOk());
	}

	@Test
	public void UpcomingMoviesHtml() throws Exception {
		Movie movie = Movie.builder()
				.name("Fairuzi Adventures")
				.description("Petualangan seorang Fairuzi")
				.duration(Duration.ofMinutes(111))
				.posterUrl("sdada")
				.releaseDate(LocalDate.now().plusDays(7))
				.tmdbId(1L)
				.build();
		movie = movieRepository.save(movie);

		this.mvc.perform(get("/movies/upcoming"))
				.andExpect(status().isOk());
	}

	@Test
	public void MovieHtml() throws Exception {
		Movie movie = Movie.builder()
				.name("Fairuzi Adventures")
				.description("Petualangan seorang Fairuzi")
				.duration(Duration.ofMinutes(111))
				.posterUrl("sdada")
				.releaseDate(LocalDate.now())
				.tmdbId(1L)
				.build();
		movie = movieRepository.save(movie);

		LocalDateTime dayTime = LocalDateTime.now().withHour(10);
		LocalDateTime nightTime = LocalDateTime.now().withHour(20);
		Theatre theatre = theatreRepository.save(new Theatre("A", 50));

		MovieSession daySession = movieSessionRepository.save(new MovieSession(movie, dayTime, theatre));
		MovieSession nightSession = movieSessionRepository.save(new MovieSession(movie, nightTime, theatre));

		this.mvc.perform(get("/movie/" + movie.getId()))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetMovieSession() throws Exception {
		long duration = 111;
		Movie movie = Movie.builder()
				.name("Fairuzi Adventures")
				.description("Petualangan seorang Fairuzi")
				.duration(Duration.ofMinutes(duration))
				.posterUrl("sdada")
				.releaseDate(LocalDate.now())
				.tmdbId(1L)
				.build();
		movie = movieRepository.save(movie);

		LocalDateTime now = LocalDateTime.now();
		Theatre theatre = theatreRepository.save(new Theatre("A", 50));
		MovieSession movieSession = movieSessionRepository.save(new MovieSession(movie, now, theatre));

		this.mvc.perform(get("/api/movie/session/" + movieSession.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.movie.name", is("Fairuzi Adventures")));
	}

	@Test
	public void testSavedBookings() throws Exception {
		long duration = 111;
		Movie movie = Movie.builder()
				.name("Fairuzi Adventures")
				.description("Petualangan seorang Fairuzi")
				.duration(Duration.ofMinutes(duration))
				.posterUrl("sdada")
				.releaseDate(LocalDate.now())
				.id(1L)
				.build();

		LocalDateTime now = LocalDateTime.now();
		Theatre theatre = new Theatre("A", 50);
		theatre.createRows();
		MovieSession movieSession = new MovieSession(movie, now, theatre);
		SavedBooking savedBooking = new SavedBooking(movieSession, theatre.getRows().get(1));

		given(savedBookingRepository.findAll()).willReturn(List.of(savedBooking));

		this.mvc.perform(get("/bookings-saved"))
				.andExpect(status().isOk());
	}

	@Test
	public void testInvalidSavingBooking() throws Exception {
		this.mvc.perform(post("/saving-booking")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("sessionId", "1")
				.param("seatIds", "3F"))
                .andExpect(status().is4xxClientError());
	}

}