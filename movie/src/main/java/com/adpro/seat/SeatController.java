package com.adpro.seat;

import com.adpro.movie.Movie;
import com.adpro.movie.MovieRepository;
import com.adpro.movie.MovieService;
import com.adpro.movie.MovieSession;
import com.adpro.movie.MovieSessionRepository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SeatController {
    TheatreRepository theatreRepository;
    MovieSessionRepository movieSessionRepository;
    MovieRepository movieRepository;
    SavedBookingRepository savedBookingRepository;
    SeatRepository seatRepository;
    private MovieService movieService;

    public SeatController(TheatreRepository theatreRepository,
                          MovieSessionRepository movieSessionRepository,
                          MovieRepository movieRepository,
                          SavedBookingRepository savedBookingRepository,
                          SeatRepository seatRepository,
                          MovieService movieService) {
        this.theatreRepository = theatreRepository;
        this.movieRepository = movieRepository;
        this.movieSessionRepository = movieSessionRepository;
        this.savedBookingRepository = savedBookingRepository;
        this.seatRepository = seatRepository;
        this.movieService = movieService;
    }

    @GetMapping("/seat")
    public @ResponseBody List<Theatre> seatApi() {
        return this.theatreRepository.findAll();
    }

    @GetMapping("/bookings-saved")
    public @ResponseBody
    Iterable<SavedBooking> savedBookingsApi() {
        return this.savedBookingRepository.findAll();
    }

    @PostMapping("/saving-booking")
    public @ResponseBody Iterable<SavedBooking> saveBooking(
            @RequestParam("sessionId") Long sessionId,
            @RequestParam("seatId") List<Integer> seatIds) {
        MovieSession movieSessionTemp = this.movieSessionRepository.findById(sessionId).get();
        List<Seat> seatTemp = this.seatRepository.findAllById(seatIds);
        List<SavedBooking> seatBookList = new ArrayList<>();
        for (Seat seat : seatTemp) {
            seatBookList.add(new SavedBooking(movieSessionTemp, seat));
        }
        return this.savedBookingRepository.saveAll(seatBookList);
    }

    @GetMapping("/showing-seat/{movieSessionId}")
    public String showSeat(@PathVariable Long movieSessionId, Model model) {
        List<MovieSession> todayMovieSessions = movieService.getTodayMovieSessions(movieSessionId);
        MovieSession movieSession = movieService.getMovieSession(movieSessionId);
        Movie movie = movieSession.getMovie();
        model.addAttribute("movie", movie);
        model.addAttribute("movieSession", movieSession);
        return "show-seat";
    }

}
