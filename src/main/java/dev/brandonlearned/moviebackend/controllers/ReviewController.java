package dev.brandonlearned.moviebackend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.brandonlearned.moviebackend.models.Review;
import dev.brandonlearned.moviebackend.services.ReviewService;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	@PostMapping
	public ResponseEntity<Review> createReview(@RequestBody Review review){
		return new ResponseEntity<Review>(
				reviewService.createReview(review), 
				HttpStatus.CREATED);
	}
	
}
