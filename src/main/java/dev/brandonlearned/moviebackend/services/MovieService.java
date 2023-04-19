package dev.brandonlearned.moviebackend.services;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brandonlearned.moviebackend.models.Movie;
import dev.brandonlearned.moviebackend.repositories.MovieRepository;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;
	
	public List<Movie> getAllMovies(){
		return movieRepository.findAll();
	}
	
	public Optional<Movie> getMovieById(String id) {
		return movieRepository.findByImdbId(id);
	}
	
}
