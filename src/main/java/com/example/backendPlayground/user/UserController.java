package com.example.backendPlayground.user;

import com.example.backendPlayground.exceptions.InvalidRequestDataException;
import com.example.backendPlayground.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping(value="/users", produces = "application/json")
	public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
										 @RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<User> allUsers = userRepository.findAll(pageable);
		return new ResponseEntity<>(
				Map.of("users", allUsers.getContent(),
						"totalItems", allUsers.getTotalElements(),
						"numberOfItems", allUsers.getNumberOfElements(),
						"totalPages", allUsers.getTotalPages(),
						"currentPage", allUsers.getNumber(),
						"size", allUsers.getSize())
				, HttpStatus.OK
		);
	}

	@GetMapping(value="/users/{userId}", produces = "application/json")
	public ResponseEntity<?> getUserById(@PathVariable Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist"));
		return ResponseEntity.ok().body(user);
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