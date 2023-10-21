package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import ru.otus.homework.domain.Author;
import ru.otus.homework.rest.dto.AuthorDto;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface AuthorMapper {

	AuthorDto toDto(Author author);

	List<AuthorDto> toDtoList(List<Author> authors);

	Author toDomain(AuthorDto authorDto);

}
