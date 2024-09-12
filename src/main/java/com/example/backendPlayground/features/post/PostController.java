package com.example.backendPlayground.features.post;

import com.example.backendPlayground.enums.PostVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

	@Autowired
	private PostService postService;

	@GetMapping(value="/posts", produces = "application/json")
	public ResponseEntity<?> getAllPosts() {
		List<PostDTO> allPosts = postService.getAllPosts();
		return ResponseEntity.ok().body(allPosts);
	}

	@GetMapping(value="/posts/{postId}", produces = "application/json")
	public ResponseEntity<?> getPostById(@PathVariable Long postId) {
		PostDTO post = postService.getPostById(postId);
		return ResponseEntity.ok().body(post);
	}

	@GetMapping(value="/users/{userId}/posts", produces = "application/json")
	public ResponseEntity<?> getPostsByUser(
			@PathVariable Long userId,
			@RequestParam(required = false) PostVisibility visibility,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String content) {

		if (visibility == null && title == null && content == null) {
			List<PostDTO> posts = postService.getAllPostsByUser(userId);
			return ResponseEntity.ok().body(posts);
		}

		List<PostDTO> filteredPosts = postService.getFilteredPostsByUserId(userId, visibility, title, content);
		return ResponseEntity.ok().body(filteredPosts);
	}

	@PostMapping("/users/{userId}/posts")
	public ResponseEntity<?> addNewPost(@PathVariable Long userId, @RequestBody Post post) {
		PostDTO savedPost = postService.addNewPost(userId, post);
		return ResponseEntity.ok().body(savedPost);
	}

	@PutMapping("/users/{userId}/posts/{postId}")
	public ResponseEntity<?> updatePost(@PathVariable Long userId, @PathVariable Long postId, @RequestBody Post post) {
		PostDTO updatedPost = postService.updatePost(userId, postId, post);
		return ResponseEntity.ok().body(updatedPost);
	}

	@DeleteMapping("/users/{userId}/posts/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable Long userId, @PathVariable Long postId) {
		postService.deletePost(userId, postId);
		return ResponseEntity.ok().build();
	}

}