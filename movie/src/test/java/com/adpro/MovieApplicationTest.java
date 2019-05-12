package com.adpro;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adpro.movie.Movie;
import com.adpro.movie.MovieListProxy;
import com.adpro.movie.MovieRepository;
import com.adpro.movie.MovieSession;
import com.adpro.movie.MovieSessionRepository;
import com.adpro.seat.FarSeat;
import com.adpro.seat.MiddleSeat;
import com.adpro.seat.Seat;
import com.adpro.seat.Theatre;
import com.adpro.seat.TheatreRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TestConfig.class })
@AutoConfigureMockMvc
public class MovieApplicationTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MovieListProxy movieListProxy;

	@MockBean
	private MovieSessionRepository movieSessionRepository;

	@MockBean
	private TheatreRepository theatreRepository;

	@MockBean
	private MovieRepository movieRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testGetMovies() throws Exception {
		Movie movie = Movie.builder()
				.name("Fairuzi Adventures")
				.description("Petualangan seorang Fairuzi")
				.duration(Duration.ofMinutes(111))
				.posterUrl("sdada")
				.releaseDate(LocalDate.now())
				.id(1L)
				.build();

		given(movieListProxy.findMoviesByReleaseDateAfterAndReleaseDateBefore(any(), any()))
				.willReturn(List.of(movie));

		this.mvc.perform(get("/api/movies/showing"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(1)))
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
				.id(1L)
				.build();

		LocalDateTime dayTime = LocalDateTime.of(1999, 8, 10, 10, 0);
		LocalDateTime nightTime = LocalDateTime.of(1999, 8, 10, 19, 0);
		MovieSession daySession = new MovieSession(movie, dayTime);
		MovieSession nightSession = new MovieSession(movie, nightTime);

		given(movieSessionRepository.findMovieSessionsByMovieIdAndStartTimeAfter(any(), any()))
				.willReturn(List.of(daySession, nightSession));

		mvc.perform(get("/api/movie/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].startTime",
						is(dayTime.format(DateTimeFormatter.ISO_DATE_TIME))))
				.andExpect(jsonPath("$[1].startTime",
						is(nightTime.format(DateTimeFormatter.ISO_DATE_TIME))));
	}

    @Test
    public void createTheatreAndSeat() {
        Theatre theatre1 = new Theatre(1, "A", 50);
        Seat seat = new MiddleSeat(false);
        theatre1.addSeatToRow(seat);
    }

	@Test
	public void checkSetSeatCost() {
		FarSeat.setCost(FarSeat.getCost()+10000);
		MiddleSeat.setCost(MiddleSeat.getCost()+10000);
	}

	@Test
    public void checkBookingSeatAvailable() {
        Theatre theatre1 = new Theatre(1, "A", 50);
        Seat seat = new MiddleSeat(false);
        theatre1.addSeatToRow(seat);
        theatre1.getRows().get(0).booked();
        theatre1.getRows().get(0).unbooked();
    }

	@Test
    public void synchronizeAPIWithTheatreAndSeat() throws Exception {
        Theatre theatre1 = new Theatre(1, "CGV", 50);
        theatre1.createRows();

		given(theatreRepository.findAll())
				.willReturn(List.of(theatre1));

        this.mvc.perform(get("/seat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].theatreNumber", is(theatre1.getTheatreNumber())))
                .andExpect(jsonPath("$[0].description", is(theatre1.getDescription())))
                .andExpect(jsonPath("$[0].seatCount", is(theatre1.getSeatCount())))
                .andExpect(jsonPath("$[0].rows[0].type", is(theatre1.getRows().get(0).getType())))
                .andExpect(jsonPath("$[0].rows[0].booked", is(theatre1.getRows().get(0).isBooked())));
    }

    @Test
    public void ShowingSeat() throws Exception {
        this.mvc.perform(get("/showing-seat"))
                .andExpect(status().isOk());
    }

	@Test
	public void MoviesHtml() throws Exception {
		Movie movie = Movie.builder()
				.name("Fairuzi Adventures")
				.description("Petualangan seorang Fairuzi")
				.duration(Duration.ofMinutes(111))
				.posterUrl("sdada")
				.releaseDate(LocalDate.now())
				.id(1L)
				.build();

		given(movieListProxy.findMoviesByReleaseDateAfterAndReleaseDateBefore(any(), any()))
			.willReturn(List.of(movie));

		this.mvc.perform(get("/movies/showing"))
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
				.id(1L)
				.build();

		LocalDateTime dayTime = LocalDateTime.of(1999, 8, 10, 10, 0);
		LocalDateTime nightTime = LocalDateTime.of(1999, 8, 10, 19, 0);
		MovieSession daySession = new MovieSession(movie, dayTime);
		MovieSession nightSession = new MovieSession(movie, nightTime);

		given(movieSessionRepository.findMovieSessionsByMovieIdAndStartTimeAfter(any(), any()))
				.willReturn(List.of(daySession, nightSession));
		given(movieRepository.findMovieById(1L))
				.willReturn(movie);

		this.mvc.perform(get("/movie/1"))
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
				.id(1L)
				.build();

		LocalDateTime now = LocalDateTime.now();
		MovieSession movieSession = new MovieSession(movie, now);

		given(movieSessionRepository.findById(any())).willReturn(Optional.of(movieSession));

		this.mvc.perform(get("/api/movie/session/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.movie.name", is("Fairuzi Adventures")));
	}
}
