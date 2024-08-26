package com.example.backendPlayground.post;

import com.example.backendPlayground.exceptions.InvalidRequestDataException;
import com.example.backendPlayground.exceptions.ResourceNotFoundException;
import com.example.backendPlayground.user.User;
import com.example.backendPlayground.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping(value="/posts", produces = "application/json")
	public ResponseEntity<?> getAllPosts() {
		Iterable<Post> allPosts = postRepository.findAll();
		return ResponseEntity.ok().body(allPosts);
	}

	@GetMapping(value="/users/{userId}/posts", produces = "application/json")
	public ResponseEntity<?> getAllPostsByUser(@PathVariable Long userId) {
		if (userRepository.findById(userId).isEmpty()) {
			throw new ResourceNotFoundException("User with id " + userId + " does not exist");
		}
		Iterable<Post> allPosts = postRepository.findByUserId(userId);
		return ResponseEntity.ok().body(allPosts);
	}

	@PostMapping("/users/{userId}/posts")
	public ResponseEntity<?> addNewPost (@PathVariable Long userId, @RequestBody Post post) {

		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist"));		if (post.getId() != null) {
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
		Post savedPost = postRepository.save(post);
		return ResponseEntity.ok().body(savedPost);
	}

}