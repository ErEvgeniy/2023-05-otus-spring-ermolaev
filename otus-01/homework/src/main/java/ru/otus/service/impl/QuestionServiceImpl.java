package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.dao.QuestionDao;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.exception.QuestionProcessingException;
import ru.otus.service.QuestionService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

	private final QuestionDao questionDao;

	@Override
	public void loadQuestions(List<List<String>> questionsParts) {
		List<Question> questions = parseQuestions(questionsParts);
		questionDao.saveQuestions(questions);
	}

	private List<Question> parseQuestions(List<List<String>> questionsParts) {
		List<Question> questions = new ArrayList<>();
		for (List<String> questionParts : questionsParts) {
			if (questionParts.size() % 2 == 0) {
				throw new QuestionProcessingException("Invalid question structure");
			}

			Question question = new Question();
			question.setText(questionParts.get(0));
			for (int i = 1; i < questionParts.size(); i += 2) {
				Answer answer = new Answer();
				answer.setText(questionParts.get(i));
				answer.setRight(Boolean.parseBoolean(questionParts.get(i + 1)));
				question.addAnswer(answer);
			}
			questions.add(question);
		}
		return questions;
	}

	@Override
	public List<Question> getQuestions() {
		return questionDao.getQuestions();
	}
}
