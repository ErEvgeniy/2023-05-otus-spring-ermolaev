package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.homework.converter.CommentConverter;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.exception.DataNotFoundException;
import ru.otus.homework.service.CommentService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class CommentShellCommands {

	private final CommentService commentService;

	private final CommentConverter commentConverter;

	@ShellMethod(value = "Print comment list", key = {"cl", "comment-list"})
	public String commentList() {
		List<Comment> comments = commentService.findAllComments();
		StringBuilder builder = new StringBuilder("Existed comments:").append(System.lineSeparator());
		for (Comment comment : comments) {
			builder.append(commentConverter.getCommentWithId(comment)).append(System.lineSeparator());
		}
		return builder.toString();
	}

	@ShellMethod(value = "Print comment by id", key = {"c", "comment-by-id"})
	public String commentById(@ShellOption long commentId) {
		try {
			Comment comment = commentService.findCommentById(commentId);
			return commentConverter.getCommentWithId(comment);
		} catch (DataNotFoundException ex) {
			return ex.getMessage();
		}
	}

	@ShellMethod(value = "Delete comment by id", key = {"cd", "comment-delete"})
	public String commentDelete(@ShellOption long commentId) {
		try {
			commentService.deleteCommentById(commentId);
			return String.format("Comment with id: %d successfully deleted", commentId);
		} catch (DataIntegrityViolationException ex) {
			return String.format("Comment with id: %d is used. Unable to delete", commentId);
		}
	}

	@ShellMethod(value = "Create new comment", key = {"cc", "comment-create"})
	public String commentCreate(
		@ShellOption(value = { "-t", "--text" }) String commentText,
		@ShellOption(value = { "-b", "--bookId" }) long bookId
	) {
		Comment comment = Comment.builder()
			.text(commentText)
			.build();

		Comment createdComment;
		try {
			createdComment = commentService.createComment(comment, bookId);
		} catch (DataNotFoundException ex) {
			return ex.getMessage();
		}
		return String.format("Comment created: %s", commentConverter.getCommentWithId(createdComment));
	}

	@ShellMethod(value = "Update existed comment", key = {"cu", "comment-update"})
	public String commentUpdate(
		@ShellOption(value = { "-i", "--id" }) long id,
		@ShellOption(defaultValue = ShellOption.NULL, value = { "-t", "--text" }) String newText
	) {
		Comment comment = Comment.builder()
			.id(id)
			.text(newText)
			.build();

		Comment updatedComment;
		try {
			updatedComment = commentService.updateComment(comment);
		} catch (DataNotFoundException ex) {
			return ex.getMessage();
		}
		return String.format("Comment updated: %s", commentConverter.getCommentWithId(updatedComment));
	}

}
