package com.example.backendPlayground.user;

import com.example.backendPlayground.post.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Entity
@Data
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private Date dateOfBirth;

	@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
	private List<Post> posts;
}