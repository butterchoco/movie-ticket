package com.adpro.movie;

import com.adpro.seat.Theatre;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class MovieSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;

    @OneToOne
    @NotNull
    private Movie movie;

    @ManyToOne
    @NotNull
    private Theatre theatre;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    public MovieSession(Movie movie, LocalDateTime startTime, Theatre theatre) {
        this.movie = movie;
        this.startTime = startTime;
        this.endTime = startTime.plus(movie.getDuration());
        this.theatre = theatre;
    }
}
