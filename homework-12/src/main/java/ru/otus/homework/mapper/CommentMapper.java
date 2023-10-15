package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.dto.CommentDto;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface CommentMapper {

	CommentDto toDto(Comment comment);

	List<CommentDto> toDtoList(List<Comment> comments);

	Comment toDomain(CommentDto commentDto);

}
