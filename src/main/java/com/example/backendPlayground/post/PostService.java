package com.example.backendPlayground.post;

import com.example.backendPlayground.exceptions.InvalidRequestDataException;
import com.example.backendPlayground.exceptions.ResourceNotFoundException;
import com.example.backendPlayground.user.User;
import com.example.backendPlayground.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	public Iterable<Post> getAllPosts() {
		return postRepository.findAll();
	}

	public Iterable<Post> getAllPostsByUser(Long userId) {
		if (userRepository.findById(userId).isEmpty()) {
			throw new ResourceNotFoundException("User with id " + userId + " does not exist");
		}
		return postRepository.findByUserId(userId);
	}

	public Post addNewPost(Long userId, Post post) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist"));

		if (post.getId() != null) {
			throw new InvalidRequestDataException("New post must not have an id");
		}
		if (post.getTitle() == null || post.getTitle().isEmpty()) {
			throw new IllegalArgumentException("Post must have a title");
		}
		if (post.getContent() == null || post.getContent().isEmpty()) {
			throw new IllegalArgumentException("Post must have content");
		}

		post.setUser(user);
		post.setDateCreated(new java.sql.Date(System.currentTimeMillis()));
		post.setDateUpdated(new java.sql.Date(System.currentTimeMillis()));
		return postRepository.save(post);
	}}
