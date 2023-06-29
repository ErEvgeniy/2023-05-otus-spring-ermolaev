package ru.otus.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Question {

	private String text;

	private List<Answer> answers = new ArrayList<>();

	public void addAnswer(Answer answer) {
		this.answers.add(answer);
	}

}
