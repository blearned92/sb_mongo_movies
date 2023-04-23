package dev.brandonlearned.moviebackend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brandonlearned.moviebackend.models.UserEntity;
import dev.brandonlearned.moviebackend.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	public UserEntity getUserByUsername(UserEntity user){
		Optional<UserEntity> response = userRepository.findByUsername(user.getUsername());
		if(response.isPresent()) {
			//check password
			if(response.get().getPassword().equals(user.getPassword())) {
				UserEntity returnUser = response.get();
				return returnUser; //pulls the user out of the optional				
			} else {
				UserEntity returnUser = new UserEntity();
				returnUser.setUsername("Invalid credentials");
				return returnUser;
			}
		} else {
			//return blank user
			UserEntity returnUser = new UserEntity();
			returnUser.setUsername("User Not Found");
			return returnUser;
		}
	}
	
}
