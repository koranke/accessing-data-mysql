package com.example.backendPlayground.post;

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
	private PostService postService;

	@GetMapping(value="/posts", produces = "application/json")
	public ResponseEntity<?> getAllPosts() {
		Iterable<Post> allPosts = postService.getAllPosts();
		return ResponseEntity.ok().body(allPosts);
	}

	@GetMapping(value="/users/{userId}/posts", produces = "application/json")
	public ResponseEntity<?> getAllPostsByUser(@PathVariable Long userId) {
		Iterable<Post> allPosts = postService.getAllPostsByUser(userId);
		return ResponseEntity.ok().body(allPosts);
	}

	@PostMapping("/users/{userId}/posts")
	public ResponseEntity<?> addNewPost(@PathVariable Long userId, @RequestBody Post post) {
		Post savedPost = postService.addNewPost(userId, post);
		return ResponseEntity.ok().body(savedPost);
	}

}