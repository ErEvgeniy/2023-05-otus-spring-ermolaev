package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.service.CommentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final BookRepository bookRepository;

	@Override
	public Comment findCommentById(String id) {
		Optional<Comment> commentOptional = bookRepository.findCommentById(id);
		return commentOptional.orElseThrow(
			() -> new DataNotFoundException(String.format("Comment with id: %s not found", id)));
	}

	@Override
	public List<Comment> findAllComments() {
		return bookRepository.findAllComments();
	}

	@Override
	public List<Comment> findAllCommentsByBook(String bookId) {
		return bookRepository.findCommentsByBookId(bookId);
	}

	@Override
	public Comment createComment(Comment comment, String bookId) {
		Optional<Book> book = bookRepository.findById(bookId);
		if (book.isEmpty()) {
			throw new DataNotFoundException(
				String.format("Book with id %s not found to save comment", bookId));
		}
		return bookRepository.saveComment(comment, book.get());
	}

	@Override
	public Comment updateComment(Comment comment) {
		Optional<Book> book = bookRepository.findBookByCommentId(comment.getId());
		if (book.isEmpty()) {
			throw new DataNotFoundException(
				String.format("Book with comment id %s not found to update", comment.getId()));
		}
		return bookRepository.saveComment(comment, book.get());
	}

	@Override
	public void deleteCommentById(String id) {
		bookRepository.deleteCommentById(id);
	}

}
