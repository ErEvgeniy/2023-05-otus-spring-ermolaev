package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.converter.QuestionConverter;
import ru.otus.domain.Question;
import ru.otus.service.ExaminationService;
import ru.otus.service.OutputService;
import ru.otus.service.QuestionService;

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
