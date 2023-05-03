package dev.brandonlearned.moviebackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.brandonlearned.moviebackend.services.UserService;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping
	public String checkUsernameTaken(@PathParam(value = "username") String username) {
		return userService.checkUsernameTaken(username);
	}	
}
