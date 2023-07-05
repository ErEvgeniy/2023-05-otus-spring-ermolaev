package ru.otus.homework.converter;

import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

public class QuestionConverter {

	public String convertToStringWithAnswers(Question question) {
		StringBuilder builder = new StringBuilder(question.getText()).append(System.lineSeparator());
		for (Answer answer : question.getAnswers()) {
			builder.append("\t").append("- ").append(answer.getText()).append(System.lineSeparator());
		}
		return builder.toString();
	}

}
