package ru.otus.homework.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.rest.dto.CommentDto;
import ru.otus.homework.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/library/v1")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@GetMapping("/comment/book/{id}")
	public List<CommentDto> commentList(@PathVariable long id) {
		return commentService.findAllCommentsByBookId(id);
	}

	@PostMapping("/comment")
	public void bookCreate(@RequestBody CommentDto commentDto, @RequestParam long bookId) {
		commentService.createComment(commentDto, bookId);
	}

	@PatchMapping("/comment")
	public void bookUpdate(@RequestBody CommentDto commentDto) {
		commentService.updateComment(commentDto);
	}

	@DeleteMapping("/comment/{id}")
	public void bookDelete(@PathVariable long id) {
		commentService.deleteCommentById(id);
	}

}
