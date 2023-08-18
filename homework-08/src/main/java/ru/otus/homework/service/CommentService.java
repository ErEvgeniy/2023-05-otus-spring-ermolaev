package ru.otus.homework.service;

import ru.otus.homework.domain.Comment;

import java.util.List;

public interface CommentService {

	Comment findCommentById(String id);

	List<Comment> findAllComments();

	List<Comment> findAllCommentsByBook(String bookId);

	Comment createComment(Comment comment, String bookId);

	Comment updateComment(Comment comment);

	void deleteCommentById(String id);

}
