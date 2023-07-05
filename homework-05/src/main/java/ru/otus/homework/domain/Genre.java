package ru.otus.homework.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Genre {

	private long id;

	private String name;

}
