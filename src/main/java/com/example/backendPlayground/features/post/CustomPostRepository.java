package com.example.backendPlayground.features.post;

import com.example.backendPlayground.enums.PostVisibility;

import java.util.List;

public interface CustomPostRepository {

	List<PostDTO> findByUserIdAndCriteria(Long userId, PostVisibility visibility, String title, String content);

	Post update(Post post);

}
