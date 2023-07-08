package ru.otus.homework.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.homework.dao.impl.CsvQuestionDao;
import ru.otus.homework.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CsvQuestionDao.class)
@TestPropertySource(value = {"classpath:application.properties"})
class CsvQuestionDaoTest {

	@Autowired
	private QuestionDao csvQuestionDao;

	@Test
	void testGetQuestionsFromCsvFile() {
		List<Question> questionsFromCsv = csvQuestionDao.getQuestions();
		assertEquals(5, questionsFromCsv.size());
	}

}
