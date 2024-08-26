package com.example.accessingDataApp.post;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

	Iterable<Post> findByUserId(Long userId);
}