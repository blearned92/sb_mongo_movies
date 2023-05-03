package dev.brandonlearned.moviebackend.repositories;

import java.util.List; 
import java.util.Optional; 

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import dev.brandonlearned.moviebackend.models.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, ObjectId>{
	Optional<UserEntity> findByUsername(String username);

	@Query(value="{'role':'USER'}")
	List<UserEntity> findAllByRole();
	
	
}
