package com.adpro.movie;

import com.adpro.movie.tmdb.FullTMDBMovie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
public class Movie implements Serializable {

    static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/w500";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;

    @JsonIgnore
    @NotNull
    private Long tmdbId;

    @NotNull
    private String name;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String description;

    @NotNull
    private String posterUrl;

    @NotNull
    private LocalDate releaseDate;

    @NotNull
    private Duration duration;

    protected Movie() {}

    public Movie(@NotNull Long tmdbId,
                 @NotNull String name,
                 @NotNull String description,
                 @NotNull String posterUrl,
                 @NotNull LocalDate releaseDate,
                 @NotNull Duration duration) {
        this.tmdbId = tmdbId;
        this.name = name;
        this.description = description;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public static Movie fromTMDBMovie(@NotNull FullTMDBMovie movie) {
        Long tmdbId = movie.getId();
        String name = movie.getOriginalTitle();
        String description = movie.getOverview();
        String posterUrl = BASE_POSTER_URL + movie.getPosterPath();
        LocalDate releaseDate = movie.getReleaseDate();
        Duration duration = movie.getDuration();
        return new Movie(tmdbId, name, description, posterUrl, releaseDate, duration);
    }

    /**
     * A hackish trick to format duration as HH:MM:SS
     * @return the LocalTime representation of duration
     */
    @JsonProperty("duration")
    public LocalTime getDurationTime() {
        return LocalTime.of(0, 0, 0).plus(duration);
    }

    @JsonIgnore
    public Duration getDuration() {
        return duration;
    }
}
