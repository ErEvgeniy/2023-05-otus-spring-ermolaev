package ru.otus.homework.dao.impl;

import org.springframework.stereotype.Component;
import ru.otus.homework.configuration.AppProperties;
import ru.otus.homework.configuration.QuestionProperties;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.util.ExtendedResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class QuestionDaoImpl implements QuestionDao {

	private final ExtendedResourceBundleMessageSource messageSource;

	private final QuestionProperties questionProperties;

	private final AppProperties appProperties;

	public QuestionDaoImpl(
		ExtendedResourceBundleMessageSource messageSource,
		QuestionProperties questionProperties,
		AppProperties appProperties
	) {
		this.messageSource = messageSource;
		this.questionProperties = questionProperties;
		this.appProperties = appProperties;
	}

	@Override
	public List<Question> getQuestions() {
		Map<String, String> rawQuestions = this.messageSource.getBundleMessages(
			questionProperties.getPath(),
			appProperties.getLocale());

		List<List<String>> questionParts = parseQuestionRows(
			rawQuestions.values(),
			questionProperties.getSeparator());

		return parseQuestions(questionParts);
	}

	private List<List<String>> parseQuestionRows(Collection<String> questionRows, String separator) {
		List<List<String>> rows = new ArrayList<>();
		for (String row : questionRows) {
			String[] rowParts = row.split(separator);
			rows.add(Arrays.asList(rowParts));
		}
		return rows;
	}

	private List<Question> parseQuestions(List<List<String>> questionsParts) {
		List<Question> questions = new ArrayList<>();
		for (List<String> questionParts : questionsParts) {
			Question question = new Question();
			question.setText(questionParts.get(0));
			int answerId = 1;
			List<Answer> answers = new ArrayList<>();
			for (int i = 1; i < questionParts.size(); i += 2) {
				Answer answer = new Answer();
				answer.setId(answerId);
				answer.setText(questionParts.get(i));
				answer.setRight(Boolean.parseBoolean(questionParts.get(i + 1)));
				answers.add(answer);
				answerId++;
			}
			question.setAnswers(answers);
			questions.add(question);
		}
		return questions;
	}

}
