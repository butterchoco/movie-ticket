package com.adpro.seat;

import com.adpro.movie.MovieSession;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Data
@Entity
@Table(name="SavedBooking")
public class SavedBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name= "SessionId", updatable=false)
    @Fetch(FetchMode.JOIN)
    private MovieSession movieSession;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name= "SeatId", updatable=false)
    @Fetch(FetchMode.JOIN)
    private Seat seat;

    public SavedBooking(MovieSession movieSession, Seat seat) {
        this.movieSession = movieSession;
        this.seat = seat;
    }

    public SavedBooking() {}

    public MovieSession getMovieSession() {
        return movieSession;
    }

    public void setMovieSession(MovieSession movieSession) {
        this.movieSession = movieSession;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
