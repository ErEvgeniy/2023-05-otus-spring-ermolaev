package ru.otus.homework.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Question {

	private String text;

	private List<Answer> answers;

	public boolean isRightAnswer(int answerId) {
		for (Answer answer : this.answers) {
			if (answer.getId() == answerId && answer.isRight()) {
				return true;
			}
		}
		return false;
	}

}
