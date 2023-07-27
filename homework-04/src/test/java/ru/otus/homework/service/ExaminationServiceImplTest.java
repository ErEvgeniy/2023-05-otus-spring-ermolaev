package ru.otus.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.converter.QuestionConverter;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.Student;
import ru.otus.homework.service.impl.ExaminationServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExaminationServiceImplTest {

	@Mock
	private QuestionService questionService;

	@Mock
	private QuestionConverter questionConverter;

	@Mock
	private IOService ioService;

	@Mock
	private MessageService messageService;

	@InjectMocks
	private ExaminationServiceImpl examinationService;

	@Test
	void shouldPrintResultWithRightAnswer() {
		when(questionService.getQuestions()).thenReturn(List.of(getDummyQuestion()));
		when(questionConverter.convertToStringWithAnswers(any(Question.class))).thenReturn("Question 1: Answer 1, Answer 2");
		when(messageService.getMessage(anyString()))
			.thenReturn("Please enter number of answer");
		when(messageService.getMessage(anyString(), any()))
			.thenReturn("Start examination for: Test Testov")
			.thenReturn("Results of examination for: Test Testov. Right answers: 1. Wrong answers: 0.");
		doNothing().when(ioService).outputString(anyString());
		when(ioService.readIntWithPrompt(anyString())).thenReturn(1);

		examinationService.doExamination(getDummyStudent());

		verify(questionService, times(1)).getQuestions();
		verify(questionConverter, times(1)).convertToStringWithAnswers(any(Question.class));
		verify(messageService, times(1)).getMessage(anyString());
		verify(messageService, times(2)).getMessage(anyString(), any());
		verify(ioService, times(3)).outputString(anyString());
		verify(ioService, times(1)).readIntWithPrompt(anyString());

		InOrder outputResults = inOrder(ioService);
		outputResults.verify(ioService)
			.outputString("Start examination for: Test Testov");
		outputResults.verify(ioService)
			.outputString("Question 1: Answer 1, Answer 2");
		outputResults.verify(ioService)
			.outputString("Results of examination for: Test Testov. Right answers: 1. Wrong answers: 0.");
	}

	@Test
	void shouldPrintResultWithWrongAnswer() {
		when(questionService.getQuestions()).thenReturn(List.of(getDummyQuestion()));
		when(questionConverter.convertToStringWithAnswers(any(Question.class))).thenReturn("Question 1: Answer 1, Answer 2");
		when(messageService.getMessage(anyString()))
			.thenReturn("Please enter number of answer");
		when(messageService.getMessage(anyString(), any()))
			.thenReturn("Start examination for: Test Testov")
			.thenReturn("Results of examination for: Test Testov. Right answers: 0. Wrong answers: 1.");
		doNothing().when(ioService).outputString(anyString());
		when(ioService.readIntWithPrompt(anyString())).thenReturn(2);

		examinationService.doExamination(getDummyStudent());

		verify(questionService, times(1)).getQuestions();
		verify(questionConverter, times(1)).convertToStringWithAnswers(any(Question.class));
		verify(messageService, times(1)).getMessage(anyString());
		verify(messageService, times(2)).getMessage(anyString(), any());
		verify(ioService, times(3)).outputString(anyString());
		verify(ioService, times(1)).readIntWithPrompt(anyString());

		InOrder outputResults = inOrder(ioService);
		outputResults.verify(ioService)
			.outputString("Start examination for: Test Testov");
		outputResults.verify(ioService)
			.outputString("Question 1: Answer 1, Answer 2");
		outputResults.verify(ioService)
			.outputString("Results of examination for: Test Testov. Right answers: 0. Wrong answers: 1.");
	}

	private Student getDummyStudent() {
		Student student = new Student();
		student.setFirstname("Test");
		student.setLastname("Testov");
		return student;
	}

	private Question getDummyQuestion() {
		Question question = new Question();
		question.setText("QuestionText");
		List<Answer> answers = List.of(
			new Answer(1, "Answer 1", true),
			new Answer(2, "Answer 2", false)
		);
		question.setAnswers(answers);
		return question;
	}

}
