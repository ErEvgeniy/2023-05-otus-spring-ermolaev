package ru.otus.homework.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Author {

	private long id;

	private String firstname;

	private String patronymic;

	private String lastname;

}
