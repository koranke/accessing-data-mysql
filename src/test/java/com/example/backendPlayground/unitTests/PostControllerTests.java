package com.example.backendPlayground.unitTests;

import com.example.backendPlayground.enums.PostVisibility;
import com.example.backendPlayground.post.Post;
import com.example.backendPlayground.post.PostController;
import com.example.backendPlayground.post.PostRepository;
import com.example.backendPlayground.user.User;
import com.example.backendPlayground.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostRepository postRepository;

	@MockBean
	private UserRepository userRepository;

	private User user;
	private Post post;

	@BeforeEach
	public void setup() {
		user = new User();
		user.setId(1L);
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("tuser@outlook.com");

		post = new Post();
		post.setId(1L);
		post.setTitle("Test Title");
		post.setContent("Test Content");
		post.setVisibility(PostVisibility.PUBLIC);
		post.setUser(user);
	}

	@Test
	public void testGetAllPosts() throws Exception {
		mockMvc.perform(get("/api/posts")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetAllPostsByUser() throws Exception {
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		mockMvc.perform(get("/api/users/1/posts")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testAddNewPost() throws Exception {
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		Mockito.when(postRepository.save(Mockito.any(Post.class))).thenReturn(post);

		String newPostJson = "{\"title\":\"New Post\",\"content\":\"New Content\",\"visibility\":\"PUBLIC\"}";

		mockMvc.perform(post("/api/users/1/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newPostJson))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"id\":1,\"title\":\"Test Title\",\"content\":\"Test Content\",\"visibility\":\"PUBLIC\"}"));
	}

	@Test
	public void testAddNewPostInvalidUser() throws Exception {
		Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

		String newPostJson = "{\"title\":\"New Post\",\"content\":\"New Content\",\"visibility\":\"PUBLIC\"}";

		mockMvc.perform(post("/api/users/1/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newPostJson))
				.andExpect(status().isNotFound());
	}
}