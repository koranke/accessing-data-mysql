package com.example.backendPlayground.post;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

	Iterable<Post> findByUserId(Long userId);
}