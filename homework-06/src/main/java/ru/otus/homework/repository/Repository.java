package ru.otus.homework.repository;

import ru.otus.homework.domain.IdEntity;

public interface Repository<E extends IdEntity> {

	E save(E t);

	void remove(E t);

}
