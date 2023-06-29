package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import ru.otus.config.AppConfig;
import ru.otus.converter.QuestionConverter;
import ru.otus.domain.Question;
import ru.otus.service.CsvService;
import ru.otus.service.ExaminationService;
import ru.otus.service.OutputService;
import ru.otus.service.QuestionService;
import ru.otus.service.ResourceService;

import java.util.List;

@RequiredArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {

	private final QuestionService questionService;

	private final OutputService outputService;

	private final ResourceService resourceService;

	private final AppConfig config;

	private final QuestionConverter questionConverter;

	private final CsvService csvService;

	@Override
	public void doExamination() {
		Resource questionResource = resourceService.getResource(config.getQuestionResourcePath());
		List<List<String>> questionsParts = csvService.parseCsvResource(
			questionResource,
			config.getQuestionSeparator());
		questionService.loadQuestions(questionsParts);

		List<Question> questions = questionService.getQuestions();
		for (Question question : questions) {
			outputService.outputString(questionConverter.convertToStringWithAnswers(question));
		}
	}

}
