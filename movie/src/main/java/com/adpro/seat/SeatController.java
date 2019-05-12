package com.adpro.seat;

import com.adpro.movie.Movie;
import com.adpro.movie.MovieRepository;
import com.adpro.movie.MovieSession;
import com.adpro.movie.MovieSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SeatController {
    @Autowired TheatreRepository theatreRepository;
    @Autowired MovieSessionRepository movieSessionRepository;
    @Autowired MovieRepository movieRepository;

    @GetMapping("/seat")
    public @ResponseBody List<Theatre> seatAPI() { return this.theatreRepository.findAll(); }

    @GetMapping("/showing-seat/{theatreId}/{movieId}")
    public String showSeat(@PathVariable Integer theatreId, @PathVariable Long movieId, Model model) {
        LocalDateTime midnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        List<MovieSession> movieSessions = movieSessionRepository.findMovieSessionsByMovieIdAndStartTimeAfter(movieId, midnight);
        System.out.println(movieSessions.size());
        Theatre theatre = theatreRepository.findTheatreById(theatreId);
        Movie movie = movieRepository.findMovieById(movieId);
        model.addAttribute("movie", movie);
        model.addAttribute("movieSessions", movieSessions);
        model.addAttribute("theatre", theatre);
        return "show-seat";
    }

}
