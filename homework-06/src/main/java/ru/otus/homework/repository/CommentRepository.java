package ru.otus.homework.repository;

import ru.otus.homework.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends Repository<Comment> {

	List<Comment> findAll();

	Optional<Comment> findById(Long id);

}
