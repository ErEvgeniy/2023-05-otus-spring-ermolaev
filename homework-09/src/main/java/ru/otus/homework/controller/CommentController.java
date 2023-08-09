package ru.otus.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.mapper.CommentMapper;
import ru.otus.homework.service.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	private final CommentMapper commentMapper;

	@GetMapping("/comment/list/{id}")
	public String commentList(@PathVariable long id, Model model) {
		List<Comment> comments = commentService.findAllCommentsByBookId(id);
		List<CommentDto> commentDtoList = commentMapper.toDtoList(comments);
		model.addAttribute("comments", commentDtoList);
		return "comment/list";
	}

}
