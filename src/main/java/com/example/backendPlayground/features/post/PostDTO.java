package com.example.backendPlayground.features.post;

import com.example.backendPlayground.enums.PostVisibility;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostDTO {
	private Long id;
	private Long userId;
	private String title;
	private String content;
	private PostVisibility visibility;
	private Timestamp dateCreated;
	private Timestamp dateUpdated;

	public PostDTO() {
	}

	public PostDTO(Post post) {
		this.id = post.getId();
		this.userId = post.getUser().getId();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.visibility = post.getVisibility();
		this.dateCreated = post.getDateCreated();
		this.dateUpdated = post.getDateUpdated();
	}
}
