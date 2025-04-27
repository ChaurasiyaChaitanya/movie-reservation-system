package com.project.movie_reservation_system.entity;

import java.time.LocalDate;
import java.util.List;

import com.project.movie_reservation_system.enums.Genre;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Movie {
	
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long movieId;

    private String movieName;

    @Enumerated(value = EnumType.STRING)
    private Genre movieGenre;

    private String movieDirector;

    private LocalDate movieReleaseDate;

    private String movieDescription;

    private Long movieDuration;

    private Integer totalBookings;

    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL)
    List<Show> shows;
}

