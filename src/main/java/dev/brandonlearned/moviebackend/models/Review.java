package dev.brandonlearned.moviebackend.models;

import java.sql.Timestamp;
import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

	@Id
	private ObjectId id;
	private String imdbId;
	private String originalPoster;
	private String body;
	private long timestamp;
	
	public Review(String imdbId, String originalPoster, String body) {
		this.imdbId = imdbId;
		this.originalPoster = originalPoster;
		this.body = body;
	}
	
	
}
