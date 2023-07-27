package ru.otus.homework.converter;

import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Comment;

@Component
public class CommentConverter {

	public String getCommentWithId(Comment comment) {
		return String.format("%d - %s", comment.getId(), comment.getText());
	}

}
