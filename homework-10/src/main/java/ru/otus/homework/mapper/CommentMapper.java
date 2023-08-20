package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.rest.dto.CommentDto;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface CommentMapper {

	@Mapping(target = "bookId", source = "book.id")
	CommentDto toDto(Comment comment);

	List<CommentDto> toDtoList(List<Comment> comments);

	Comment toDomain(CommentDto commentDto);

}
