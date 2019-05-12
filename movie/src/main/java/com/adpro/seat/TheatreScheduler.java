package com.adpro.seat;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TheatreScheduler {
    TheatreRepository theatreRepository;

    @Autowired
    public TheatreScheduler(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    @PostConstruct
    public void createTheatre() {
        Long count = theatreRepository.count();
        if (count == 0) {
            List<Theatre> theatreList = new ArrayList<>();
            theatreList.add(new Theatre("A", 50));
            theatreList.add(new Theatre("B", 40));
            theatreList.add(new Theatre("C", 60));
            theatreList.add(new Theatre("D", 70));
            theatreList.add(new Theatre("E", 70));
            theatreList.add(new Theatre("F", 70));
            theatreRepository.saveAll(theatreList);
        }
    }

}
