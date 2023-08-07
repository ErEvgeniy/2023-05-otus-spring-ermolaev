package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;
import ru.otus.homework.service.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;

	private final BookRepository bookRepository;

	@Override
	public Comment findCommentById(String id) {
		Optional<Comment> commentOptional = commentRepository.findById(id);
		return commentOptional.orElseThrow(
			() -> new DataNotFoundException(String.format("Comment with id: %s not found", id)));
	}

	@Override
	public List<Comment> findAllComments() {
		return commentRepository.findAll();
	}

	@Override
	public List<Comment> findAllCommentsByBook(String bookId) {
		return commentRepository.findAllByBookId(bookId);
	}

	@Override
	public Comment createComment(Comment comment, String bookId) {
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		if (optionalBook.isEmpty()) {
			throw new DataNotFoundException(
				String.format("Book with id %s not found to save comment", bookId));
		}
		Book book = optionalBook.get();
		comment.setBook(book);

		if (book.getComments() == null) {
			List<Comment> comments = new ArrayList<>();
			comments.add(comment);
			book.setComments(comments);
		} else {
			book.getComments().add(comment);
		}

		commentRepository.save(comment);
		bookRepository.save(book);
		return comment;
	}

	@Override
	public Comment updateComment(Comment comment) {
		Optional<Book> book = bookRepository.findBookByCommentsIs(comment.getId());
		if (book.isEmpty()) {
			throw new DataNotFoundException(
				String.format("Book with comment id %s not found to update", comment.getId()));
		}
		comment.setBook(book.get());
		return commentRepository.save(comment);
	}

	@Override
	public void deleteCommentById(String id) {
		Optional<Comment> comment = commentRepository.findById(id);
		if (comment.isEmpty()) {
			throw new DataNotFoundException(
				String.format("Comment with id %s not found to delete", id));
		}
		commentRepository.deleteById(id);

		Optional<Book> optionalBook = bookRepository.findBookByCommentsIs(id);
		optionalBook.ifPresent(book -> {
			book.getComments().remove(comment.get());
			bookRepository.save(book);
		});
	}

}
