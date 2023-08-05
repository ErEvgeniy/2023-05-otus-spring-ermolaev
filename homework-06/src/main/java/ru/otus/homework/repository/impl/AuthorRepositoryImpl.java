package ru.otus.homework.repository.impl;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.homework.repository.AbstractRepository;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.domain.Author;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryImpl extends AbstractRepository<Author> implements AuthorRepository {

	@Override
	public Optional<Author> findById(Long id) {
		TypedQuery<Author> query = getEm().createQuery("SELECT a FROM Author a WHERE a.id = :id", Author.class);
		query.setParameter("id", id);
		try {
			return Optional.of(query.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Author> findAll() {
		return getEm().createQuery("SELECT a FROM Author a", Author.class).getResultList();
	}

}
