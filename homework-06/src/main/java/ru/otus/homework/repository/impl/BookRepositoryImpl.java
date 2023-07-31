package ru.otus.homework.repository.impl;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
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

	@Override
	public List<Book> findAllWithComments() {
		List<Book> books = findAll();
		if (books != null && !books.isEmpty()) {
			books = loadComments(books);
		}
		return books;
	}

	private List<Book> loadComments(List<Book> books) {
		CriteriaBuilder cb = getEm().getCriteriaBuilder();
		CriteriaQuery<Book> cq = cb.createQuery(Book.class);
		Root<Book> root = cq.from(Book.class);
		root.fetch("comments", JoinType.LEFT);

		ParameterExpression<List> booksParam = cb.parameter(List.class);
		cq.select(root)
			.distinct(true)
			.where(root.in(booksParam));

		TypedQuery<Book> query = getEm().createQuery(cq);
		query.setParameter(booksParam, books);
		return query.getResultList();
	}

}
