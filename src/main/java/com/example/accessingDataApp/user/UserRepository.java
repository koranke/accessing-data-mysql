package com.example.accessingDataApp.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

	Optional<Object> findByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);
}