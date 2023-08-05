package ru.otus.homework.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExaminationInfo {

	private Student student;

	private List<Question> rightAnsweredQuestions;

	private List<Question> wrongAnsweredQuestions;

}
