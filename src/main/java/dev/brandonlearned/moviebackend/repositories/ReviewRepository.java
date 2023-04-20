package dev.brandonlearned.moviebackend.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.brandonlearned.moviebackend.models.Review;

@Repository
public interface ReviewRepository extends MongoRepository<Review, ObjectId> {

}
