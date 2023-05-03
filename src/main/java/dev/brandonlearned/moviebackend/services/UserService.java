package dev.brandonlearned.moviebackend.services;

import java.util.List; 
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.brandonlearned.moviebackend.models.UserEntity;
import dev.brandonlearned.moviebackend.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	public String checkUsernameTaken(String username){
		Optional<UserEntity> response = userRepository.findByUsername(username);
		if(response.isPresent()) {
			return response.get().getUsername();
		} else {
			return "";
		}
	}

	public List<UserEntity> findAllByRole() {
		List<UserEntity> users = userRepository.findAllByRole();
		return users;
	}
	
	//delete user account
	
}
