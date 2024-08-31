package com.example.backendPlayground.unitTests;

import com.example.backendPlayground.enums.PostVisibility;
import com.example.backendPlayground.post.Post;
import com.example.backendPlayground.post.PostController;
import com.example.backendPlayground.post.PostDTO;
import com.example.backendPlayground.post.PostService;
import com.example.backendPlayground.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostService postService;

	@Autowired
	private ObjectMapper objectMapper;

	private User user;
	private PostDTO post;
	private String postJson;
	private String postJsonResponse;

	@BeforeEach
	public void setup() throws Exception {
		user = new User();
		user.setId(1L);
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("tuser@outlook.com");

		post = new PostDTO();
		post.setId(1L);
		post.setTitle("Test Title");
		post.setContent("Test Content");
		post.setVisibility(PostVisibility.PUBLIC);
		post.setUserId(1L);

		postJson = objectMapper.writeValueAsString(post);
		postJsonResponse = objectMapper.writeValueAsString(Collections.singletonList(post));
	}

	@Test
	public void testGetAllPosts() throws Exception {
		Mockito.when(postService.getAllPosts()).thenReturn(Collections.singletonList(post));

		mockMvc.perform(get("/api/posts")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(postJsonResponse));
	}

	@Test
	public void testGetAllPostsByUser() throws Exception {
		Mockito.when(postService.getAllPostsByUser(1L)).thenReturn(Collections.singletonList(post));

		mockMvc.perform(get("/api/users/1/posts")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(postJsonResponse));
	}

	@Test
	public void testAddNewPost() throws Exception {
		Mockito.when(postService.addNewPost(Mockito.eq(1L), Mockito.any(Post.class))).thenReturn(post);

		Post newPost = objectMapper.readValue(postJson, Post.class);
		newPost.setId(null);
		String newPostJson = objectMapper.writeValueAsString(newPost);

		mockMvc.perform(post("/api/users/1/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newPostJson))
				.andExpect(status().isOk())
				.andExpect(content().json(postJson));
	}
}