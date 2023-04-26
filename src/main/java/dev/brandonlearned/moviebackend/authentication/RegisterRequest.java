package dev.brandonlearned.moviebackend.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//this class describes the expected object to be passed in to the register endpoint
public class RegisterRequest {
	
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	
}
