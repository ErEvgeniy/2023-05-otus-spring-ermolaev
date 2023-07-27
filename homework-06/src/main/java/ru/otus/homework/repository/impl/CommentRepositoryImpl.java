package ru.otus.homework.repository.impl;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.repository.AbstractRepository;
import ru.otus.homework.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl extends AbstractRepository<Comment> implements CommentRepository {

	@Override
	public Optional<Comment> findById(Long id) {
		TypedQuery<Comment> query = getEm().createQuery("SELECT c FROM Comment c " +
			"WHERE c.id = :id", Comment.class);
		query.setParameter("id", id);
		try {
			return Optional.of(query.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Comment> findAll() {
		return getEm().createQuery("SELECT c FROM Comment c", Comment.class).getResultList();
	}

}
