package com.example.backendPlayground.user;

import lombok.Data;

import java.sql.Date;

@Data
public class UserDTO {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private Date dateOfBirth;

	public UserDTO() {
	}

	public UserDTO(User user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.phone = user.getPhone();
		this.dateOfBirth = user.getDateOfBirth();
	}

	public User toUserEntity() {
		User user = new User();
		user.setId(this.id);
		user.setFirstName(this.firstName);
		user.setLastName(this.lastName);
		user.setEmail(this.email);
		user.setPhone(this.phone);
		user.setDateOfBirth(this.dateOfBirth);
		return user;
	}
}
