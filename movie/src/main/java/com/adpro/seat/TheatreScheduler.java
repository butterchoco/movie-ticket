package com.adpro.seat;

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
            Theatre theatre1 = new Theatre("A", 50);
            Theatre theatre2 = new Theatre("B", 40);
            Theatre theatre3 = new Theatre("C", 60);
            Theatre theatre4 = new Theatre("D", 70);
            theatre1.createRows();
            theatre2.createRows();
            theatre3.createRows();
            theatre4.createRows();

            theatreRepository.saveAll(List.of(theatre1,theatre2,theatre3,theatre4));
        }
    }

}
