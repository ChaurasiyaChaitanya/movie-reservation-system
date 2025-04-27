package com.project.movie_reservation_system.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieRequestDTO {
    private String movieName;

    private String movieGenre;

    private String movieDirector;

    private LocalDate movieReleaseDate;

    private String movieDescription;

    private Long movieDuration;

}
