package dev.brandonlearned.moviebackend.repositories;

import java.util.Optional;

import org.bson.types.ObjectId; 
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.brandonlearned.moviebackend.models.Movie;

@Repository
public interface MovieRepository extends MongoRepository<Movie, ObjectId>{
	Optional<Movie> findByImdbId(String imdbId);
}
