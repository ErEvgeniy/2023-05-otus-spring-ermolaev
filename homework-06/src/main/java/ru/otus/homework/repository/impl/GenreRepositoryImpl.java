package ru.otus.homework.repository.impl;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.homework.repository.AbstractRepository;
import ru.otus.homework.repository.GenreRepository;
import ru.otus.homework.domain.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryImpl extends AbstractRepository<Genre> implements GenreRepository {

	@Override
	public Optional<Genre> findById(Long id) {
		TypedQuery<Genre> query = getEm().createQuery("SELECT g FROM Genre g WHERE g.id = :id", Genre.class);
		query.setParameter("id", id);
		try {
			return Optional.of(query.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Genre> findAll() {
		return getEm().createQuery("SELECT g FROM Genre g", Genre.class).getResultList();
	}

}
