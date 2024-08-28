package com.example.backendPlayground.user;

import com.example.backendPlayground.exceptions.InvalidRequestDataException;
import com.example.backendPlayground.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public Map<String, Object> getAllUsers(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<User> allUsers = userRepository.findAll(pageable);
		return Map.of(
				"users", allUsers.getContent(),
				"totalItems", allUsers.getTotalElements(),
				"numberOfItems", allUsers.getNumberOfElements(),
				"totalPages", allUsers.getTotalPages(),
				"currentPage", allUsers.getNumber(),
				"size", allUsers.getSize()
		);
	}

	public User getUserById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist"));
	}

	public User addNewUser(User user) {
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
		if (userRepository.findByFirstNameAndLastNameAndEmail(user.getFirstName(), user.getLastName(), user.getEmail()).isPresent()) {
			throw new InvalidRequestDataException("User with same first name, last name and email already exists");
		}

		return userRepository.save(user);
	}
}
