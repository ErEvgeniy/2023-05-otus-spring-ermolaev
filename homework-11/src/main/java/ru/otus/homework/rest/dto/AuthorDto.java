package ru.otus.homework.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

	private String id;

	@NotBlank
	private String firstname;

	private String patronymic;

	@NotBlank
	private String lastname;

}
