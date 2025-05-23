package com.project.movie_reservation_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.movie_reservation_system.dto.APIResponseDTO;
import com.project.movie_reservation_system.dto.MovieRequestDTO;
import com.project.movie_reservation_system.dto.PagedAPIResponseDTO;
import com.project.movie_reservation_system.entity.Movie;
import com.project.movie_reservation_system.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    MovieController(MovieService movieService)
    {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public ResponseEntity<PagedAPIResponseDTO> getAllMovies(
            @RequestParam int page,
            @RequestParam int pageSize
    )
    {
        Page<Movie> movies = movieService.getAllMovies(page,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        PagedAPIResponseDTO
                                .builder()
                                .pageData(movies.getContent())
                                .totalElements(movies.getTotalElements())
                                .totalPages(movies.getTotalPages())
                                .currentLimit(movies.getNumberOfElements())
                                .build()
                );
    }

    @GetMapping("/movie/{movieId}")
    public  ResponseEntity<APIResponseDTO> getMovieById(@PathVariable Long movieId)
    {
        Movie movie = movieService.getMovieById(movieId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .data(movie)
                                .build()
                );
    }

    @Secured("ROLE_SUPER_ADMIN")
    @PostMapping("/movie/create")
    public ResponseEntity<APIResponseDTO> createNewMovie(@RequestBody MovieRequestDTO MovieRequestDTO)
    {
        Movie newMovie = movieService.createNewMovie(MovieRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .message("New Movie created with the id: " + newMovie.getMovieId() + " and name: "+newMovie.getMovieName()+".")
                                .data(newMovie)
                                .build()
                );
    }

    @Secured("ROLE_SUPER_ADMIN")
    @PutMapping("movie/{movieId}")
    public ResponseEntity<APIResponseDTO> updateMovieById(@PathVariable Long movieId,@RequestBody MovieRequestDTO movieRequestDTO)
    {
        Movie updatedMovie = movieService.updateMovieById(movieId,movieRequestDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponseDTO.builder()
                                .message("Updated the movie with the id: " + updatedMovie.getMovieId() + " and name: "+updatedMovie.getMovieName()+".")
                                .data(updatedMovie)
                                .build()
                );
    }

    @Secured("ROLE_SUPER_ADMIN")
    @DeleteMapping("movie/{movieId}")
    public ResponseEntity<APIResponseDTO> deleteMovieById(@PathVariable Long movieId)
    {
        Movie deletedMovie = movieService.getMovieById(movieId);
        movieService.deleteMovieById(movieId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        APIResponseDTO
                                .builder()
                                .message("Deleted the movie with the id: "+deletedMovie.getMovieId()+" and name: "+deletedMovie.getMovieName()+".")
                                .build()
                );
    }
}

