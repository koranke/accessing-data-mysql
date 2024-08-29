package com.example.backendPlayground.unitTests;

import com.example.backendPlayground.exceptions.ResourceNotFoundException;
import com.example.backendPlayground.post.Post;
import com.example.backendPlayground.post.PostRepository;
import com.example.backendPlayground.post.PostService;
import com.example.backendPlayground.user.User;
import com.example.backendPlayground.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

	@InjectMocks
	private PostService postService;

	@Mock
	private PostRepository postRepository;

	@Mock
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
		post.setUser(user);
	}

	@Test
	public void testGetAllPosts() {
		postService.getAllPosts();
		Mockito.verify(postRepository).findAll();
	}

	@Test
	public void testGetAllPostsByUser() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		postService.getAllPostsByUser(1L);
		Mockito.verify(postRepository).findByUserId(1L);
	}

	@Test
	public void testAddNewPost() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(postRepository.save(any(Post.class))).thenReturn(post);

		Post newPost = new Post();
		newPost.setTitle("New Post");
		newPost.setContent("New Content");

		postService.addNewPost(1L, newPost);
		Mockito.verify(postRepository).save(any(Post.class));
	}

	@Test
	public void testAddNewPostInvalidUser() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		Post newPost = new Post();
		newPost.setTitle("New Post");
		newPost.setContent("New Content");

		assertThrows(ResourceNotFoundException.class, () -> postService.addNewPost(1L, newPost));
	}}
