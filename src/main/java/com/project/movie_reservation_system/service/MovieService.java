package com.project.movie_reservation_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.movie_reservation_system.constants.ExceptionConstants;
import com.project.movie_reservation_system.dto.MovieRequestDTO;
import com.project.movie_reservation_system.entity.Movie;
import com.project.movie_reservation_system.enums.Genre;
import com.project.movie_reservation_system.exception.MovieNotFoundException;
import com.project.movie_reservation_system.repository.MovieRepository;

@Service
public class MovieService {
	
	private final MovieRepository movieRepository;

    @Autowired
    MovieService(MovieRepository movieRepository)
    {
        this.movieRepository = movieRepository;
    }

    public Page<Movie> getAllMovies(int page, int pageSize)
    {
        return movieRepository.findAll(PageRequest.of(page,pageSize));
    }

    public Movie getMovieById(Long movieId)
    {
        return movieRepository
                .findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(ExceptionConstants.MOVIE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Movie createNewMovie(MovieRequestDTO movieRequestDTO)
    {
        Movie movie = Movie
                .builder()
                .movieName(movieRequestDTO.getMovieName())
                .movieDescription(movieRequestDTO.getMovieDescription())
                .movieDirector(movieRequestDTO.getMovieDirector())
                .movieGenre(Genre.valueOf(movieRequestDTO.getMovieGenre()))
                .movieReleaseDate(movieRequestDTO.getMovieReleaseDate())
                .totalBookings(0)
                .build();

        return movieRepository.save(movie);
    }

    public Movie updateMovieById(Long movieId, MovieRequestDTO movieRequestDTO)
    {
        return  movieRepository
                .findById(movieId)
                .map(Movie -> {
                    Movie.setMovieName(movieRequestDTO.getMovieName());
                    Movie.setMovieDescription(movieRequestDTO.getMovieDescription());
                    Movie.setMovieDuration(movieRequestDTO.getMovieDuration());
                    Movie.setMovieDirector(movieRequestDTO.getMovieDirector());
                    Movie.setMovieGenre(Genre.valueOf(movieRequestDTO.getMovieGenre()));
                    Movie.setMovieReleaseDate(movieRequestDTO.getMovieReleaseDate());
                    return movieRepository.save(Movie);
                })
                .orElseThrow(() -> new MovieNotFoundException(ExceptionConstants.MOVIE_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public void deleteMovieById(Long movieId)
    {
        movieRepository.deleteById(movieId);
    }

}
