package com.example.backendPlayground.post;

import com.example.backendPlayground.enums.PostVisibility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<PostDTO> findByUserIdAndCriteria(Long userId, PostVisibility visibility, String title, String content) {
		String queryStr = "SELECT p FROM Post p WHERE p.user.id = :userId";
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
}
