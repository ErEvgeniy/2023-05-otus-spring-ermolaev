package ru.otus.homework.dao.impl;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import ru.otus.homework.configuration.LocaleProvider;
import ru.otus.homework.configuration.QuestionProvider;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.exception.ResourceParsingException;
import ru.otus.homework.exception.UnexpectedLocaleException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CsvQuestionDao implements QuestionDao {

	private final ResourceLoader resourceLoader;

	private final LocaleProvider localeProvider;

	private final QuestionProvider questionProvider;

	public CsvQuestionDao(
		ResourceLoader resourceLoader,
		LocaleProvider localeProvider,
		QuestionProvider questionProvider
	) {
		this.resourceLoader = resourceLoader;
		this.localeProvider = localeProvider;
		this.questionProvider = questionProvider;
	}

	@Override
	public List<Question> getQuestions() {
		Resource questionResource = getResource();
		List<List<String>> questionParts = parseCsvResource(questionResource, questionProvider.getSeparator());
		return parseQuestions(questionParts);
	}

	private Resource getResource() {
		return switch (localeProvider.getLocale().getLanguage()) {
			case "en" -> resourceLoader.getResource(questionProvider.getPathEN());
			case "ru" -> resourceLoader.getResource(questionProvider.getPathRU());
			default -> throw new UnexpectedLocaleException("Unexpected locale for questions");
		};
	}

	private List<List<String>> parseCsvResource(Resource csvResource, char separator) {
		try (CSVReader reader = new CSVReader(new InputStreamReader(csvResource.getInputStream()))) {
			CSVParser parser = new CSVParserBuilder()
				.withSeparator(separator)
				.build();

			List<List<String>> rows = new ArrayList<>();

			for (String[] row : reader.readAll()) {
				String[] rowParts = parser.parseLine(row[0]);
				rows.add(Arrays.asList(rowParts));
			}

			return rows;
		} catch (IOException | CsvException ex) {
			throw new ResourceParsingException("Fail parsing csv resource", ex);
		}
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
