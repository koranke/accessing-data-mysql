package com.example.backendPlayground.post;

import com.example.backendPlayground.enums.PostVisibility;
import com.example.backendPlayground.exceptions.InvalidRequestDataException;
import com.example.backendPlayground.exceptions.ResourceNotFoundException;
import com.example.backendPlayground.user.User;
import com.example.backendPlayground.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	public List<PostDTO> getAllPosts() {
		return postRepository.findAll().stream().map(PostDTO::new).toList();
	}

	public List<PostDTO> getAllPostsByUser(Long userId) {
		if (userRepository.findById(userId).isEmpty()) {
			throw new ResourceNotFoundException("User with id " + userId + " does not exist");
		}
		return postRepository.findByUserId(userId).stream().map(PostDTO::new).toList();
	}

	public List<PostDTO> getFilteredPostsByUserId(Long userId, PostVisibility visibility, String title, String content) {
		return postRepository.findByUserIdAndCriteria(userId, visibility, title, content);
	}

	public PostDTO addNewPost(Long userId, Post post) {
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
		post.setDateCreated(new Timestamp(System.currentTimeMillis()));
		post.setDateUpdated(new Timestamp(System.currentTimeMillis()));
		if (post.getVisibility() == null) {
			post.setVisibility(PostVisibility.PRIVATE);
		}
		return new PostDTO(postRepository.save(post));
	}

	public PostDTO updatePost(Long userId, Long postId, Post post) {
		userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist"));

		Post existingPost = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post with id " + postId + " does not exist"));

		if (!existingPost.getUser().getId().equals(userId)) {
			throw new InvalidRequestDataException("User with id " + userId + " is not the owner of post with id " + postId);
		}

		if (post.getTitle() != null && !post.getTitle().isEmpty()) {
			existingPost.setTitle(post.getTitle());
		}
		if (post.getContent() != null && !post.getContent().isEmpty()) {
			existingPost.setContent(post.getContent());
		}
		if (post.getVisibility() != null) {
			existingPost.setVisibility(post.getVisibility());
		}
		existingPost.setDateUpdated(new Timestamp(System.currentTimeMillis()));

		return new PostDTO(postRepository.save(existingPost));
	}

	public void deletePost(Long userId, Long postId) {
		userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist"));

		Post existingPost = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post with id " + postId + " does not exist"));

		if (!existingPost.getUser().getId().equals(userId)) {
			throw new InvalidRequestDataException("User with id " + userId + " is not the owner of post with id " + postId);
		}

		postRepository.deleteById(postId);
	}

	public PostDTO getPostById(Long postId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post with id " + postId + " does not exist"));
		return new PostDTO(post);
	}
}
