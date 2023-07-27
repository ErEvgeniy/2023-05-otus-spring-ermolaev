package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.repository.CommentRepository;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.CommentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;

	private final BookService bookService;

	@Override
	@Transactional(readOnly = true)
	public Optional<Comment> findOptionalCommentById(long id) {
		return commentRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Comment findCommentById(long id) {
		return getComment(id);
	}

	private Comment getComment(long id) {
		Optional<Comment> commentOptional = findOptionalCommentById(id);
		return commentOptional.orElseThrow(
			() -> new DataNotFoundException(String.format("Comment with id: %d not found", id)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Comment> findAllComments() {
		return commentRepository.findAll();
	}

	@Override
	@Transactional
	public Comment createComment(Comment comment, Long bookId) {
		Book book = bookService.findBookById(bookId);
		comment.setBook(book);
		return commentRepository.save(comment);
	}

	@Override
	@Transactional
	public Comment updateComment(Comment comment) {
		Comment toUpdate = getComment(comment.getId());
		String newText = comment.getText();
		if (newText != null && !newText.isEmpty()) {
			toUpdate.setText(newText);
		}
		return commentRepository.save(toUpdate);
	}

	@Override
	@Transactional
	public void deleteCommentById(long id) {
		Comment existComment = getComment(id);
		commentRepository.remove(existComment);
	}

}
