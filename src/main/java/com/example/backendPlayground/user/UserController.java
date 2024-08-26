package com.example.backendPlayground.user;

import com.example.backendPlayground.exceptions.InvalidRequestDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping(value="/users", produces = "application/json")
	public ResponseEntity<?> getAllUsers() {
		Iterable<User> allUsers = userRepository.findAll();
		return ResponseEntity.ok().body(allUsers);
	}

	@PostMapping("/users")
	public ResponseEntity<?> addNewUser (@RequestBody User user) {
		if (user.getId() != null) {
			throw new InvalidRequestDataException("New user must not have an id");
		}
		if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
			throw new InvalidRequestDataException("User must have a first name");
		}
		if (user.getLastName() == null || user.getLastName().isEmpty()) {
			throw new InvalidRequestDataException("User must have a last name");
		}
		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			throw new InvalidRequestDataException("User must have an email");
		}
		//check if there is already a user in the db with same first name, last name and email
		if (userRepository.findByFirstNameAndLastNameAndEmail(user.getFirstName(), user.getLastName(), user.getEmail()).isPresent()) {
			throw new InvalidRequestDataException("User with same first name, last name and email already exists");
		}


		User savedUser = userRepository.save(user);
		return ResponseEntity.ok().body(savedUser);
	}

}