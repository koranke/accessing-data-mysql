package com.example.backendPlayground.post;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface PostRepository extends ListCrudRepository<Post, Long>, CustomPostRepository {

	List<Post> findByUserId(Long userId);

}