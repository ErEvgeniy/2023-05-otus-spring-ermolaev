package ru.otus.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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
	private StudentService studentService;

	@InjectMocks
	private ExaminationServiceImpl examinationService;

	@Test
	void testDoExamination() {
		when(studentService.greetingStudent()).thenReturn(getDummyStudent());
		when(questionService.getQuestions()).thenReturn(List.of(getDummyQuestion()));
		when(questionConverter.convertToStringWithAnswers(any(Question.class))).thenReturn("Question 1: Answer 1, Answer 2");
		doNothing().when(ioService).outputString(anyString());
		when(ioService.readIntWithPrompt(anyString())).thenReturn(1);
		when(questionService.isRightAnswer(any(Question.class), anyInt())).thenReturn(true);

		examinationService.doExamination();

		verify(studentService, times(1)).greetingStudent();
		verify(questionService, times(1)).getQuestions();
		verify(questionConverter, times(1)).convertToStringWithAnswers(any(Question.class));
		verify(ioService, times(3)).outputString(anyString());
		verify(ioService, times(1)).readIntWithPrompt(anyString());
		verify(questionService, times(1)).isRightAnswer(any(Question.class), anyInt());
	}

	private Student getDummyStudent() {
		Student student = new Student();
		student.setFirstname("Test");
		student.setFirstname("Testov");
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
