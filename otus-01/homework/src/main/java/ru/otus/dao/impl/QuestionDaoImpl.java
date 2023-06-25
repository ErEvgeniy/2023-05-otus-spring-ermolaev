package ru.otus.dao.impl;

import ru.otus.dao.QuestionDao;
import ru.otus.domain.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionDaoImpl implements QuestionDao {

	private final List<Question> questionList = new ArrayList<>();

	@Override
	public void saveQuestions(List<Question> questions) {
		this.questionList.addAll(questions);
	}

	@Override
	public List<Question> getQuestions() {
		return this.questionList;
	}

}
