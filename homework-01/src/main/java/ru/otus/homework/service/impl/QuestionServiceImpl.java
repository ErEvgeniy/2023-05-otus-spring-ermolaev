package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Question;
import ru.otus.homework.service.QuestionService;

import java.util.List;

@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

	private final QuestionDao questionDao;

	@Override
	public List<Question> getQuestions() {
		return questionDao.getQuestions();
	}
}
