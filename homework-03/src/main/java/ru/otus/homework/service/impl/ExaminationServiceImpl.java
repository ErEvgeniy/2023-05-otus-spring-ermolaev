package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.constant.MessageCode;
import ru.otus.homework.converter.QuestionConverter;
import ru.otus.homework.domain.ExaminationInfo;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.Student;
import ru.otus.homework.service.ExaminationService;
import ru.otus.homework.service.IOService;
import ru.otus.homework.service.MessageService;
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

	private final MessageService messageService;

	@Override
	public void doExamination() {
		Student student = studentService.greetingStudent();
		ioService.outputString(messageService.getMessage(
			MessageCode.EXAM_START,
			new String[]{student.getFullName()}));
		List<Question> questions = questionService.getQuestions();
		ExaminationInfo examinationInfo = askQuestions(questions);
		examinationInfo.setStudent(student);

		ioService.outputString(messageService.getMessage(MessageCode.EXAM_RESULT,
			new String[]{
				examinationInfo.getStudent().getFullName(),
				String.valueOf(examinationInfo.getRightAnsweredQuestions().size()),
				String.valueOf(examinationInfo.getWrongAnsweredQuestions().size())
			}));
	}

	private ExaminationInfo askQuestions(List<Question> questionsToAsk) {
		ExaminationInfo examinationInfo = new ExaminationInfo();
		List<Question> rightAnsweredQuestions = new ArrayList<>();
		List<Question> wrongAnsweredQuestions = new ArrayList<>();

		for (Question question : questionsToAsk) {
			ioService.outputString(questionConverter.convertToStringWithAnswers(question));
			String askMessage = messageService.getMessage(MessageCode.EXAM_ASK_ANSWER);
			int answerId = ioService.readIntWithPrompt(askMessage);
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
