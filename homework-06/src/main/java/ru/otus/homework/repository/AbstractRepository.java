package ru.otus.homework.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.otus.homework.domain.IdEntity;

public abstract class AbstractRepository<E extends IdEntity> implements Repository<E> {

	@PersistenceContext
	private EntityManager em;

	protected EntityManager getEm() {
		return em;
	}

	@Override
	public E save(E entity) {
		if (entity.getId() == null) {
			em.persist(entity);
			return entity;
		} else {
			return em.merge(entity);
		}
	}

	@Override
	public void remove(E entity) {
		em.remove(entity);
	}

}
