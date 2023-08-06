package ru.otus.homework.repository;

import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryCustom {

	Optional<Comment> findCommentById(String commentId);

	List<Comment> findCommentsByBookId(String bookId);

	List<Comment> findAllComments();

	void deleteCommentById(String commentId);

	Comment saveComment(Comment comment, Book book);

	Optional<Book> findBookByCommentId(String commentId);

}
