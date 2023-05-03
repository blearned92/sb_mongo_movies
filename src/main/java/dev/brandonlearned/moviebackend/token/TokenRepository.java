package dev.brandonlearned.moviebackend.token;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<Token, ObjectId>{

	//get all tokens by user id where token is not expired
	@Query(value="{'expired':false, 'revoked':false, 'user._id':ObjectId('?0')}")
	List<Token> findAllValidTokensByUserId(ObjectId userId);
//	
	Optional<Token> findByToken(String token);
}
