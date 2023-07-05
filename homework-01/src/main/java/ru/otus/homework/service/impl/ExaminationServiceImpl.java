package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.converter.QuestionConverter;
import ru.otus.homework.domain.Question;
import ru.otus.homework.service.ExaminationService;
import ru.otus.homework.service.OutputService;
import ru.otus.homework.service.QuestionService;

import java.util.List;

@RequiredArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {

	private final QuestionService questionService;

	private final OutputService outputService;

	private final QuestionConverter questionConverter;

	@Override
	public void doExamination() {
		List<Question> questions = questionService.getQuestions();
		for (Question question : questions) {
			outputService.outputString(questionConverter.convertToStringWithAnswers(question));
		}
	}

}
