package ru.otus.homework.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

	private String firstname;

	private String lastname;

	public String getFullName() {
		return String.format("%s %s", firstname, lastname);
	}
}
