package ru.otus.homework.repository.impl;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.homework.repository.AbstractRepository;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.domain.Book;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl extends AbstractRepository<Book> implements BookRepository {

	@Override
	public Optional<Book> findById(Long id) {
		TypedQuery<Book> query = getEm().createQuery("SELECT b FROM Book b " +
			"LEFT JOIN FETCH b.author " +
			"LEFT JOIN FETCH b.genre " +
			"WHERE b.id = :id", Book.class);
		query.setParameter("id", id);
		try {
			return Optional.of(query.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Book> findAll() {
		TypedQuery<Book> query = getEm().createQuery("SELECT b FROM Book b " +
			"LEFT JOIN FETCH b.author " +
			"LEFT JOIN FETCH b.genre", Book.class);
		return query.getResultList();
	}

}
