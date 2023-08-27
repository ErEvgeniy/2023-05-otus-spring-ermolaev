package ru.otus.homework.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

	private String id;

	@NotBlank
	private String name;

	@NotNull
	private GenreDto genre;

	@NotNull
	private AuthorDto author;

}
