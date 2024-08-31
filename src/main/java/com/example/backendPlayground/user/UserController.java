package com.example.backendPlayground.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping(value="/users", produces = "application/json")
	public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
										 @RequestParam(defaultValue = "10") int size) {
		Map<String, Object> response = userService.getAllUsers(page, size);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value="/users/{userId}", produces = "application/json")
	public ResponseEntity<?> getUserById(@PathVariable Long userId) {
		UserDTO user = userService.getUserById(userId);
		return ResponseEntity.ok().body(user);
	}

	@PostMapping("/users")
	public ResponseEntity<?> addNewUser(@RequestBody User user) {
		UserDTO savedUser = userService.addNewUser(user);
		return ResponseEntity.ok().body(savedUser);
	}

	@PutMapping("/users/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user) {
		UserDTO updatedUser = userService.updateUser(userId, user);
		return ResponseEntity.ok().body(updatedUser);
	}

	@DeleteMapping("/users/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return ResponseEntity.ok().build();
	}

}