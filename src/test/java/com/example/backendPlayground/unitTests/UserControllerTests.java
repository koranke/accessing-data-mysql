package com.example.backendPlayground.unitTests;

import com.example.backendPlayground.user.User;
import com.example.backendPlayground.user.UserController;
import com.example.backendPlayground.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	private User user;

	@BeforeEach
	public void setup() {
		user = new User();
		user.setId(1L);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.doe@example.com");
	}

	@Test
	public void testGetAllUsers() throws Exception {
		Pageable pageable = PageRequest.of(0, 10);
		Page<User> pagedResponse = new PageImpl<>(Collections.singletonList(user), pageable, 1);

		Mockito.when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pagedResponse);

		mockMvc.perform(get("/api/users")
						.param("page", "0")
						.param("size", "10")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"totalItems\":1,\"totalPages\":1,\"currentPage\":0,\"users\":[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"phone\":null,\"dateOfBirth\":null}],\"numberOfItems\":1,\"size\":10}"))
		;
	}

	@Test
	public void testGetUserById() throws Exception {
		Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

		mockMvc.perform(get("/api/users/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\"}"));
	}

	@Test
	public void testAddNewUser() throws Exception {
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

		String newUserJson = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\"}";

		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newUserJson))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\"}"));
	}

	@Test
	public void testAddNewUserInvalidData() throws Exception {
		String newUserJson = "{\"firstName\":\"\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\"}";

		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newUserJson))
				.andExpect(status().isBadRequest());
	}
}