package ru.otus.homework.service;

import ru.otus.homework.rest.dto.CommentDto;

import java.util.List;

public interface CommentService {

	CommentDto findCommentById(long id);

	List<CommentDto> findAllComments();

	List<CommentDto> findAllCommentsByBookId(Long bookId);

	CommentDto createComment(CommentDto comment);

	CommentDto updateComment(CommentDto comment);

	void deleteCommentById(long id);

}
