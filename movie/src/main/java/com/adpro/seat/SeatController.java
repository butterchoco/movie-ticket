package com.adpro.seat;

import com.adpro.movie.MovieRepository;
import com.adpro.movie.MovieSession;
import com.adpro.movie.MovieSessionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


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
    public @ResponseBody List<Theatre> seatApi() {
        return this.theatreRepository.findAll();
    }

    @GetMapping("/bookings-saved")
    public @ResponseBody
    Iterable<SavedBooking> savedBookingsApi() {
        return this.savedBookingRepository.findAll();
    }

    @PostMapping("/saving-booking")
    public @ResponseBody SavedBooking saveBooking(Long movieSessionId,
                                                  Integer seatId,
                                                  Integer theatreId) {
        MovieSession movieSessionTemp = this.movieSessionRepository.findById(movieSessionId).get();
        Seat seatTemp = this.seatRepository.findById(seatId).get();
        SavedBooking savedBooking = new SavedBooking(movieSessionTemp, seatTemp);
        return this.savedBookingRepository.save(savedBooking);
    }

    @GetMapping("/showing-seat/{sessionId}")
    public ModelAndView showSeat(@PathVariable Long sessionId) {
        ModelAndView view = new ModelAndView("show-seat");
        LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        MovieSession session = movieSessionRepository.findById(sessionId).get();
        List<MovieSession> movieSessions = movieSessionRepository
                .findMovieSessionsByMovieIdAndStartTimeAfter(
                        session.getMovie().getId(), midnight);
        view.addAllObjects(Map.of(
                "movieSessions", movieSessions,
                "movie", session.getMovie(),
                "theatre", session.getTheatre(),
                "sessionId", session.getId()));
        if (session != null) {
            return view;
        } else {
            return new ModelAndView("redirect:/movies");
        }
    }

}
