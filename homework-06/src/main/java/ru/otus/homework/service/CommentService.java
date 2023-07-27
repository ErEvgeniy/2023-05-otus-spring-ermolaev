package ru.otus.homework.service;

import ru.otus.homework.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

	Optional<Comment> findOptionalCommentById(long id);

	Comment findCommentById(long id);

	List<Comment> findAllComments();

	Comment createComment(Comment comment, Long bookId);

	Comment updateComment(Comment comment);

	void deleteCommentById(long id);

}
