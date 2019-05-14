package com.adpro.movie.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public abstract class TmdbMovie {

    static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/w500";
    static final String NO_POSTER_URL = "/img/no-poster.png";

    @NotNull
    @JsonProperty("id")
    private Long id;

    @NotNull
    @JsonProperty("original_title")
    private String originalTitle;

    @NotNull
    @JsonProperty("overview")
    private String overview;

    @NotNull
    @JsonProperty("poster_path")
    private String posterPath;

    @NotNull
    @JsonProperty("release_date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate releaseDate;


    public void setPosterPath(String posterPath) {
        if (posterPath == null) {
            this.posterPath = NO_POSTER_URL;
        } else {
            this.posterPath = BASE_POSTER_URL + posterPath;
        }
    }
}
