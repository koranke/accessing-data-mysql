package com.example.backendPlayground.post;

import com.example.backendPlayground.enums.PostVisibility;
import com.example.backendPlayground.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Post {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	private String title;
	private String content;
	@Enumerated(EnumType.STRING)
	private PostVisibility visibility;
	private Timestamp dateCreated;
	private Timestamp dateUpdated;
}

