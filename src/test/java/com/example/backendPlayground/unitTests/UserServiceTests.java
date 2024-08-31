package com.example.backendPlayground.unitTests;

import com.example.backendPlayground.exceptions.InvalidRequestDataException;
import com.example.backendPlayground.exceptions.ResourceNotFoundException;
import com.example.backendPlayground.user.User;
import com.example.backendPlayground.user.UserDTO;
import com.example.backendPlayground.user.UserRepository;
import com.example.backendPlayground.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepository userRepository;

	private UserDTO user;

	@BeforeEach
	public void setup() {
		user = new UserDTO();
		user.setId(1L);
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("tuser@outlook.com");
	}

	/*
	TODO: this test is broken. Fix it.
	 */
	@Test
	@Disabled
	public void testGetAllUsers() {
		userService.getAllUsers(0, 10);

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		Mockito.verify(userRepository).findAll(pageableCaptor.capture());

		Pageable pageable = pageableCaptor.getValue();
		assertEquals(0, pageable.getPageNumber());
		assertEquals(10, pageable.getPageSize());
	}

	@Test
	public void testGetUserById() {
		given(userRepository.findById(1L)).willReturn(Optional.of(user.toUserEntity()));
		UserDTO result = userService.getUserById(1L);
		assertEquals(user, result);
	}

	@Test
	public void testAddNewUser() {
		when(userRepository.save(any(User.class))).thenReturn(user.toUserEntity());

		User newUser = new User();
		newUser.setFirstName("New");
		newUser.setLastName("User");
		newUser.setEmail("newuser@outlook.com");

		userService.addNewUser(newUser);
		Mockito.verify(userRepository).save(any(User.class));
	}

	@Test
	public void testAddNewUserInvalidData() {
		User newUser = new User();
		newUser.setFirstName("");
		newUser.setLastName("Lastname");
		newUser.setEmail("email@j.com");

		assertThrows(InvalidRequestDataException.class, () -> userService.addNewUser(newUser));
	}

	@Test
	public void testGetUserByIdNotFound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
	}
}