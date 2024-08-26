package com.example.accessingDataApp;

import com.example.accessingDataApp.post.Post;
import com.example.accessingDataApp.post.PostRepository;
import com.example.accessingDataApp.user.User;
import com.example.accessingDataApp.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {TestcontainersConfiguration.class})
public class RepositoryIntegrationTests {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	private User user;
	private Post post;

	@BeforeEach
	public void setup() {
		user = new User();
		user.setFirstName("Test");
		user.setLastName("User");
		user.setEmail("tuser@outlook.com");
		user = userRepository.save(user);

		post = new Post();
		post.setTitle("Test Title");
		post.setContent("Test Content");
		post.setUser(user);
		post = postRepository.save(post);
	}

	@Test
	void contextLoads() {
		assertThat(userRepository).isNotNull();
		assertThat(postRepository).isNotNull();
	}

	@Test
	void testFindAllUsers() {
		List<User> users = (List<User>) userRepository.findAll();
		assertThat(users).isNotEmpty();
	}

	@Test
	void testFindAllPosts() {
		List<Post> posts = (List<Post>) postRepository.findAll();
		assertThat(posts).isNotEmpty();
	}

	@Test
	void testFindPostsByUser() {
		List<Post> posts = (List<Post>) postRepository.findByUserId(user.getId());
		assertThat(posts).isNotEmpty();
	}
}