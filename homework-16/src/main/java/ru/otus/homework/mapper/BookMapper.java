package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import ru.otus.homework.domain.Book;
import ru.otus.homework.rest.dto.BookDto;

import java.util.List;

@Mapper(config = MappingConfig.class, uses = {
	AuthorMapper.class,
	GenreMapper.class,
	CommentMapper.class
})
public interface BookMapper {

	BookDto toDto(Book book);

	List<BookDto> toDtoList(List<Book> books);

	Book toDomain(BookDto bookDto);

}
