package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.converter.QuestionConverter;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.service.impl.ExaminationServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
	private QuestionConverter questionConverter;

	@BeforeEach
	void setUp() {
		questionService = mock(QuestionService.class);
		outputService = mock(OutputService.class);
		questionConverter = mock(QuestionConverter.class);

		examinationService = new ExaminationServiceImpl(questionService, outputService, questionConverter);
	}

	@Test
	void testDoExamination() {

		Question question = new Question();
		question.setText("QuestionText");
		question.addAnswer(new Answer("Answer 1", false));
		question.addAnswer(new Answer("Answer 2", true));
		List<Question> questions = List.of(question);

		when(questionService.getQuestions())
			.thenReturn(questions);
		when(questionConverter.convertToStringWithAnswers(any(Question.class)))
			.thenReturn("Question 1: Answer 1: Answer 2");
		doNothing().when(outputService).outputString(anyString());

		examinationService.doExamination();

		verify(questionService, times(1)).getQuestions();
		verify(questionConverter, times(1)).convertToStringWithAnswers(any(Question.class));
		verify(outputService, times(1)).outputString(anyString());
	}
}
