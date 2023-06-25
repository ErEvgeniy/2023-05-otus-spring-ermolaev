package ru.otus.service;

import ru.otus.domain.Question;

import java.util.List;

public interface QuestionService {

	void loadQuestions(List<List<String>> questionsParts);

	List<Question> getQuestions();

}
