package dev.brandonlearned.moviebackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.brandonlearned.moviebackend.models.UserEntity;
import dev.brandonlearned.moviebackend.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping
	public UserEntity getUser(@RequestBody UserEntity user) {
		return userService.getUserByUsername(user);
	}
	
	//register
	//login
	
	
	
}
