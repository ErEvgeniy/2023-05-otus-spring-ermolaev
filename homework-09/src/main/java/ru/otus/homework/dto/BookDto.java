package ru.otus.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

	private Long id;

	private String name;

	private GenreDto genre;

	private AuthorDto author;

}
