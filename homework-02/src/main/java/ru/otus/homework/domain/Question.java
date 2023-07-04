package ru.otus.homework.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Question {

	private String text;

	private List<Answer> answers;

}
