package dev.brandonlearned.moviebackend.services;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import dev.brandonlearned.moviebackend.models.Movie;
import dev.brandonlearned.moviebackend.models.Review;
import dev.brandonlearned.moviebackend.repositories.ReviewRepository;

@Service
public class ReviewService {
	
	@Autowired
	ReviewRepository reviewRepository;
	
	@Autowired
	MovieService movieService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Review createReview(Review review) {
		
		Optional<Movie> movie = movieService.getMovieById(review.getImdbId());
		if(movie.isPresent()) {
			//create review			
			review.setTimestamp(Instant.now().toEpochMilli() / 1000);
			Review retrunReview = reviewRepository.insert(review);			
			//update movie class reviews array in db where imdbid matches
			mongoTemplate.update(Movie.class)
			.matching(Criteria.where("imdbId").is(review.getImdbId()))
			.apply(new Update().push("reviews").value(review))
			.first(); //only updates the first instance found that matches
			return retrunReview;
		} else {
			//create movie if not exists
			movieService.createMovie(review.getImdbId());
			review.setTimestamp(Instant.now().toEpochMilli() / 1000);
			Review retrunReview = reviewRepository.insert(review);			
			mongoTemplate.update(Movie.class)
			.matching(Criteria.where("imdbId").is(review.getImdbId()))
			.apply(new Update().push("reviews").value(review))
			.first(); //only updates the first instance found that matches
			return retrunReview;	
		}
		
		
	}
}
