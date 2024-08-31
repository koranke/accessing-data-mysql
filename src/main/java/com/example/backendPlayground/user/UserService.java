package com.example.backendPlayground.user;

import com.example.backendPlayground.exceptions.InvalidRequestDataException;
import com.example.backendPlayground.exceptions.ResourceNotFoundException;
import com.example.backendPlayground.post.PostRepository;
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
	@Autowired
	private PostRepository postRepository;

	public Map<String, Object> getAllUsers(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<User> allUsers = userRepository.findAll(pageable);
		Page<UserDTO> allUsersDTO = allUsers.map(UserDTO::new);
		return Map.of(
				"users", allUsersDTO.getContent(),
				"totalItems", allUsers.getTotalElements(),
				"numberOfItems", allUsers.getNumberOfElements(),
				"totalPages", allUsers.getTotalPages(),
				"currentPage", allUsers.getNumber(),
				"size", allUsers.getSize()
		);
	}

	public UserDTO getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist"));
		return new UserDTO(user);
	}

	public UserDTO addNewUser(User user) {
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

		return new UserDTO(userRepository.save(user));
	}

	public UserDTO updateUser(Long userId, User user) {
		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist"));

		if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
			existingUser.setFirstName(user.getFirstName());
		}
		if (user.getLastName() != null && !user.getLastName().isEmpty()) {
			existingUser.setLastName(user.getLastName());
		}
		if (user.getEmail() != null && !user.getEmail().isEmpty()) {
			existingUser.setEmail(user.getEmail());
		}
		if (user.getDateOfBirth() != null) {
			existingUser.setDateOfBirth(user.getDateOfBirth());
		}
		if (user.getPhone() != null && !user.getPhone().isEmpty()) {
			existingUser.setPhone(user.getPhone());
		}

		if (userRepository.findByFirstNameAndLastNameAndEmail(existingUser.getFirstName(), existingUser.getLastName(), existingUser.getEmail()).isPresent()) {
			throw new InvalidRequestDataException("User with same first name, last name and email already exists");
		}

		return new UserDTO(userRepository.save(existingUser));
	}

	public void deleteUser(Long userId) {
		User existingUser = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist"));

		if (!postRepository.findByUserId(userId).isEmpty()) {
			throw new InvalidRequestDataException("User with id " + userId + " has posts and cannot be deleted");
		}
		userRepository.delete(existingUser);
	}
}
