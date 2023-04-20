package dev.brandonlearned.moviebackend.services;

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
	private MongoTemplate mongoTemplate;
	
	public Review createReview(String reviewBody, String imdbId) {
		Review review = reviewRepository.insert(new Review(reviewBody));
		//updates the movie class where the imdbId passed in matches its imdbId
		//updates its reviewIds field with this review
		mongoTemplate.update(Movie.class)
			.matching(Criteria.where("imdbId").is(imdbId))
			.apply(new Update().push("reviews").value(review))
			.first(); //only updates the first instance found that matches
		
		return review;
	}
}
