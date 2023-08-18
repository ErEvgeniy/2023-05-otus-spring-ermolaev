package ru.otus.homework.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

	private Long id;

	private String firstname;

	private String patronymic;

	private String lastname;

	public String getFullName() {
		StringBuilder builder = new StringBuilder();
		if (this.firstname != null && !this.firstname.isEmpty()) {
			builder.append(this.firstname).append(" ");
		}
		if (this.patronymic != null && !this.patronymic.isEmpty()) {
			builder.append(this.patronymic).append(" ");
		}
		if (this.lastname != null && !this.lastname.isEmpty()) {
			builder.append(this.lastname);
		}
		return builder.toString();
	}
}
