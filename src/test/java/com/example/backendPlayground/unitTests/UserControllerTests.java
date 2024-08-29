package com.example.backendPlayground.unitTests;

import com.example.backendPlayground.exceptions.InvalidRequestDataException;
import com.example.backendPlayground.user.User;
import com.example.backendPlayground.user.UserController;
import com.example.backendPlayground.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	private User user;
	private String userJson;

	@BeforeEach
	public void setup() throws Exception {
		user = new User();
		user.setId(1L);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.doe@example.com");

		userJson = objectMapper.writeValueAsString(user);
	}

	@Test
	public void testGetUserById() throws Exception {
		Mockito.when(userService.getUserById(1L)).thenReturn(user);

		mockMvc.perform(get("/api/users/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(userJson));
	}

	@Test
	public void testAddNewUser() throws Exception {
		Mockito.when(userService.addNewUser(Mockito.any(User.class))).thenReturn(user);

		User newUser = objectMapper.readValue(userJson, User.class);
		newUser.setId(null);
		String newUserJson = objectMapper.writeValueAsString(newUser);

		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newUserJson))
				.andExpect(status().isOk())
				.andExpect(content().json(userJson));
	}

	@Test
	public void testAddNewUserInvalidData() throws Exception {
		String newUserJson = "{\"firstName\":\"\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\"}";

		Mockito.when(userService.addNewUser(Mockito.any(User.class)))
				.thenThrow(new InvalidRequestDataException("User must have a first name"));

		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newUserJson))
				.andExpect(status().isBadRequest());
	}
}