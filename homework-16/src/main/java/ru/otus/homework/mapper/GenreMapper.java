package ru.otus.homework.mapper;

import org.mapstruct.Mapper;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.rest.dto.GenreDto;

import java.util.List;

@Mapper(config = MappingConfig.class)
public interface GenreMapper {

	GenreDto toDto(Genre genre);

	List<GenreDto> toDtoList(List<Genre> genres);

	Genre toDomain(GenreDto genreDto);

}
