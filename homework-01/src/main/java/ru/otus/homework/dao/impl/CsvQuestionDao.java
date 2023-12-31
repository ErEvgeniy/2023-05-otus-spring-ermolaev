package ru.otus.homework.dao.impl;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.exception.ResourceParsingException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

	private final ResourceLoader resourceLoader;

	private final String questionResourcePath;

	private final char questionSeparator;

	@Override
	public List<Question> getQuestions() {
		Resource questionResource = resourceLoader.getResource(questionResourcePath);
		List<List<String>> questionParts = parseCsvResource(questionResource, questionSeparator);
		return parseQuestions(questionParts);
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

}
