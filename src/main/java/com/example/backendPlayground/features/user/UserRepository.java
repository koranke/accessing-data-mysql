package com.example.backendPlayground.features.user;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, ListCrudRepository<User, Long> {

	Optional<Object> findByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email);
}