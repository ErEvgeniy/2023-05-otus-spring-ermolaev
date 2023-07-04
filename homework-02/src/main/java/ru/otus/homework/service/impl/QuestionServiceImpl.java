package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.service.QuestionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

	private final QuestionDao questionDao;

	@Override
	public List<Question> getQuestions() {
		return questionDao.getQuestions();
	}

	@Override
	public boolean isRightAnswer(Question question, int answerId) {
		for (Answer answer : question.getAnswers()) {
			if (answer.getId() == answerId && answer.isRight()) {
				return true;
			}
		}
		return false;
	}
}
