package dev.brandonlearned.moviebackend.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.brandonlearned.moviebackend.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
	
	private String firstname;
	private String lastname;
	private String username;
	private Role role;
	@JsonProperty("access_token") //renames the response
	private String accessToken;
	@JsonProperty("refresh_token")
	private String refreshToken;
	
}
