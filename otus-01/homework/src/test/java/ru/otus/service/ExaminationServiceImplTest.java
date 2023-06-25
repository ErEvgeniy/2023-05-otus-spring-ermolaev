package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import ru.otus.config.AppConfig;
import ru.otus.converter.QuestionConverter;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.service.impl.ExaminationServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExaminationServiceImplTest {

	private ExaminationService examinationService;
	private QuestionService questionService;
	private OutputService outputService;
	private ResourceService resourceService;
	private AppConfig config;
	private QuestionConverter questionConverter;
	private CsvService csvService;

	@BeforeEach
	void setUp() {
		questionService = mock(QuestionService.class);
		outputService = mock(OutputService.class);
		resourceService = mock(ResourceService.class);
		config = mock(AppConfig.class);
		questionConverter = mock(QuestionConverter.class);
		csvService = mock(CsvService.class);

		examinationService = new ExaminationServiceImpl(questionService, outputService,
			resourceService, config, questionConverter, csvService);
	}

	@Test
	void testDoExamination() {
		Resource questionResource = mock(Resource.class);
		List<List<String>> questionsParts = List.of(Arrays.asList("QuestionText", "Answer 1", "false", "Answer 2", "true"));

		Question question = new Question();
		question.setText("QuestionText");
		question.addAnswer(new Answer("Answer 1", false));
		question.addAnswer(new Answer("Answer 2", true));
		List<Question> questions = List.of(question);

		when(config.getQuestionResourcePath())
			.thenReturn("questions.csv");
		when(config.getQuestionSeparator())
			.thenReturn(';');
		when(resourceService.getResource(anyString()))
			.thenReturn(questionResource);
		when(csvService.parseCsvResource(any(Resource.class), anyChar()))
			.thenReturn(questionsParts);
		doNothing().when(questionService).loadQuestions(questionsParts);
		when(questionService.getQuestions())
			.thenReturn(questions);
		when(questionConverter.convertToStringWithAnswers(any(Question.class)))
			.thenReturn("Question 1: Answer 1: Answer 2");
		doNothing().when(outputService).outputString(anyString());

		examinationService.doExamination();

		verify(config, times(1)).getQuestionResourcePath();
		verify(config, times(1)).getQuestionSeparator();
		verify(resourceService, times(1)).getResource(anyString());
		verify(csvService, times(1)).parseCsvResource(any(Resource.class), anyChar());
		verify(questionService, times(1)).loadQuestions(questionsParts);
		verify(questionService, times(1)).getQuestions();
		verify(questionConverter, times(1)).convertToStringWithAnswers(any(Question.class));
		verify(outputService, times(1)).outputString(anyString());
	}
}
