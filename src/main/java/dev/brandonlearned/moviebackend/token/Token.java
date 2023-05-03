package dev.brandonlearned.moviebackend.token;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import dev.brandonlearned.moviebackend.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="tokens")
public class Token {

	@Id
	private ObjectId id;
	private String token;
	private TokenType tokenType;
	private boolean expired;
	private boolean revoked;
	private UserEntity user;
}
