package com.adpro.seat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SeatController {
    SeatRepository seatRepository;
    TheatreRepository theatreRepository;

    @Autowired
    public SeatController(SeatRepository seatRepository, TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
        this.seatRepository = seatRepository;
    }

    @GetMapping("/seat")
    public @ResponseBody List<Theatre> seatAPI() { return this.theatreRepository.findAll(); }

    @RequestMapping("/showing-seat")
    public String showSeat(Model model) {
        model.addAttribute("theatre", "CGV");
        return "show-seat";
    }

}
