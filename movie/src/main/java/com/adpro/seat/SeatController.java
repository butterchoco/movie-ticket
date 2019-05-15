package com.adpro.seat;

import com.adpro.movie.Movie;
import com.adpro.movie.MovieService;
import com.adpro.movie.MovieSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SeatController {

    private TheatreRepository theatreRepository;
    private MovieService movieService;

    @Autowired
    public SeatController(TheatreRepository theatreRepository,
                          MovieService movieService) {
        this.theatreRepository = theatreRepository;
        this.movieService = movieService;
    }

    @GetMapping("/seat")
    public @ResponseBody List<Theatre> seatApi() {
        return this.theatreRepository.findAll();
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
