package ru.otus.homework.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.homework.configuration.AppConfiguration;
import ru.otus.homework.dao.impl.CsvQuestionDao;
import ru.otus.homework.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = {CsvQuestionDao.class, AppConfiguration.class})
class CsvQuestionDaoTest {

	@Autowired
	private QuestionDao questionDao;

	@Test
	void shouldGetQuestionsFromResource() {
		List<Question> questions = questionDao.getQuestions();
		assertEquals(5, questions.size());
	}

}
