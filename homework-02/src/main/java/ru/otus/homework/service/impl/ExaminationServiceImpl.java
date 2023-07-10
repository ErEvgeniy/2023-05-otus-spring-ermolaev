package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.converter.QuestionConverter;
import ru.otus.homework.domain.ExaminationInfo;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.Student;
import ru.otus.homework.service.ExaminationService;
import ru.otus.homework.service.IOService;
import ru.otus.homework.service.QuestionService;
import ru.otus.homework.service.StudentService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {

	private final QuestionService questionService;

	private final QuestionConverter questionConverter;

	private final IOService ioService;

	private final StudentService studentService;

	@Override
	public void doExamination() {
		Student student = studentService.greetingStudent();
		ioService.outputString(String.format("Start examination for: %s", student.getFullName()));
		List<Question> questions = questionService.getQuestions();
		ExaminationInfo examinationInfo = askQuestions(questions);
		examinationInfo.setStudent(student);

		String examResultMsg = "Results of examination for: %s. Right answers: %d. Wrong answers: %d.";
		ioService.outputString(String.format(examResultMsg,
			examinationInfo.getStudent().getFullName(),
			examinationInfo.getRightAnsweredQuestions().size(),
			examinationInfo.getWrongAnsweredQuestions().size()));
	}

	private ExaminationInfo askQuestions(List<Question> questionsToAsk) {
		ExaminationInfo examinationInfo = new ExaminationInfo();
		List<Question> rightAnsweredQuestions = new ArrayList<>();
		List<Question> wrongAnsweredQuestions = new ArrayList<>();

		for (Question question : questionsToAsk) {
			ioService.outputString(questionConverter.convertToStringWithAnswers(question));
			int answerId = ioService.readIntWithPrompt("Please enter number of answer");
			if (question.isRightAnswer(answerId)) {
				rightAnsweredQuestions.add(question);
			} else {
				wrongAnsweredQuestions.add(question);
			}
		}

		examinationInfo.setRightAnsweredQuestions(rightAnsweredQuestions);
		examinationInfo.setWrongAnsweredQuestions(wrongAnsweredQuestions);
		return examinationInfo;
	}

}
