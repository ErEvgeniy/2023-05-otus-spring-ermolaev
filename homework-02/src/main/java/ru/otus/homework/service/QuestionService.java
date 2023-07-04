package ru.otus.homework.service;

import ru.otus.homework.domain.Question;

import java.util.List;

public interface QuestionService {

	List<Question> getQuestions();

	boolean isRightAnswer(Question question, int answerId);

}
