package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.rest.dto.CommentDto;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.mapper.CommentMapper;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.CommentRepository;
import ru.otus.homework.service.CommentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;

	private final BookRepository bookRepository;

	private final CommentMapper commentMapper;

	@Override
	@Transactional(readOnly = true)
	public CommentDto findCommentById(long id) {
		Optional<Comment> commentOptional = commentRepository.findById(id);
		if (commentOptional.isEmpty()) {
			throw new DataNotFoundException(String.format("Comment with id: %d not found", id));
		}
		return commentMapper.toDto(commentOptional.get());
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommentDto> findAllComments() {
		List<Comment> comments = commentRepository.findAll();
		return commentMapper.toDtoList(comments);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommentDto> findAllCommentsByBookId(Long bookId) {
		List<Comment> comments = commentRepository.findAllByBookId(bookId);
		return commentMapper.toDtoList(comments);
	}

	@Override
	@Transactional
	public CommentDto createComment(CommentDto commentDto) {
		Comment newComment = commentMapper.toDomain(commentDto);
		Optional<Book> book = bookRepository.findById(commentDto.getBookId());
		book.ifPresentOrElse(newComment::setBook, () -> {
			throw new DataNotFoundException(String.format("Book with id: %d not found",
				commentDto.getBookId()));
		});

		commentRepository.save(newComment);
		return commentMapper.toDto(newComment);
	}

	@Override
	@Transactional
	public CommentDto updateComment(CommentDto commentDto) {
		Comment toUpdate = commentRepository.findById(commentDto.getId()).orElseThrow(
			() -> new DataNotFoundException(
				String.format("Comment with id: %d not found", commentDto.getId())));
		String newText = commentDto.getText();
		if (newText != null && !newText.isEmpty()) {
			toUpdate.setText(newText);
		}

		commentRepository.save(toUpdate);
		return commentMapper.toDto(toUpdate);
	}

	@Override
	@Transactional
	public void deleteCommentById(long id) {
		commentRepository.deleteById(id);
	}

}
