package ru.otus.homework.service;

import ru.otus.homework.domain.Comment;

import java.util.List;

public interface CommentService {

	Comment findCommentById(long id);

	List<Comment> findAllComments();

	List<Comment> findAllCommentsByBookId(Long bookId);

	Comment createComment(Comment comment, Long bookId);

	Comment updateComment(Comment comment);

	void deleteCommentById(long id);

}
