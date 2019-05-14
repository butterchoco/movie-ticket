package com.adpro.seat;

import com.adpro.movie.Movie;
import com.adpro.movie.MovieRepository;
import com.adpro.movie.MovieSession;
import com.adpro.movie.MovieSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
public class SeatController {
    TheatreRepository theatreRepository;
    MovieSessionRepository movieSessionRepository;
    MovieRepository movieRepository;
    SavedBookingRepository savedBookingRepository;
    SeatRepository seatRepository;

    public SeatController(TheatreRepository theatreRepository,
                          MovieSessionRepository movieSessionRepository,
                          MovieRepository movieRepository,
                          SavedBookingRepository savedBookingRepository,
                          SeatRepository seatRepository
                          ) {
        this.theatreRepository = theatreRepository;
        this.movieRepository = movieRepository;
        this.movieSessionRepository = movieSessionRepository;
        this.savedBookingRepository = savedBookingRepository;
        this.seatRepository = seatRepository;
    }

    @GetMapping("/seat")
    public @ResponseBody List<Theatre> seatAPI() { return this.theatreRepository.findAll(); }

    @GetMapping("/bookings-saved")
    public @ResponseBody
    Iterable<SavedBooking> savedBookingsAPI() { return this.savedBookingRepository.findAll(); }

    @PostMapping("/saving-booking")
    public @ResponseBody SavedBooking saveBooking(Long movieSessionId, Integer seatId, Integer theatreId) {
        MovieSession MovieSessionTemp = this.movieSessionRepository.findById(movieSessionId).get();
        Theatre TheatreTemp = this.theatreRepository.findById(theatreId).get();
        Seat SeatTemp = this.seatRepository.findById(seatId).get();
        SavedBooking savedBooking = new SavedBooking(MovieSessionTemp, TheatreTemp, SeatTemp);
        return this.savedBookingRepository.save(savedBooking);
    }

    @GetMapping("/showing-seat/{theatreId}/{movieId}")
    public ModelAndView showSeat(@PathVariable Integer theatreId, @PathVariable Long movieId, Model model) {
        ModelAndView view = new ModelAndView("show-seat");
        LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        List<MovieSession> movieSessions = movieSessionRepository.findMovieSessionsByMovieIdAndStartTimeAfter(movieId, midnight);
        Theatre theatre = theatreRepository.findById(theatreId).get();
        Movie movie = movieRepository.findMovieById(movieId);
        view.addAllObjects(Map.of("theatre", theatre, "movieSessions",movieSessions,"movie",movie));
        if (movieSessions.size() != 0)  return view;
        else return new ModelAndView("redirect:/movies");
    }

}
