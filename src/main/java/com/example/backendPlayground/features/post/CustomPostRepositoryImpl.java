package com.example.backendPlayground.features.post;

import com.example.backendPlayground.enums.PostVisibility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<PostDTO> findByUserIdAndCriteria(Long userId, PostVisibility visibility, String title, String content) {
		String queryStr = "SELECT p FROM posts p WHERE p.user.id = :userId";
		if (visibility != null) {
			queryStr += " AND p.visibility = :visibility";
		}
		if (title != null && !title.isEmpty()) {
			queryStr += " AND p.title LIKE :title";
		}
		if (content != null && !content.isEmpty()) {
			queryStr += " AND p.content LIKE :content";
		}

		TypedQuery<Post> query = entityManager.createQuery(queryStr, Post.class);
		query.setParameter("userId", userId);
		if (visibility != null) {
			query.setParameter("visibility", visibility);
		}
		if (title != null && !title.isEmpty()) {
			query.setParameter("title", "%" + title + "%");
		}
		if (content != null && !content.isEmpty()) {
			query.setParameter("content", "%" + content + "%");
		}

		return query.getResultList().stream().map(PostDTO::new).toList();
	}

	@Override
	@Transactional
	public Post update(Post post) {
		Post existingPost = entityManager.find(Post.class, post.getId());
		if (existingPost == null) {
			throw new IllegalArgumentException("Post with id " + post.getId() + " does not exist");
		}
		existingPost.setTitle(post.getTitle());
		existingPost.setContent(post.getContent());
		existingPost.setVisibility(post.getVisibility());
		return entityManager.merge(existingPost);
	}
}
