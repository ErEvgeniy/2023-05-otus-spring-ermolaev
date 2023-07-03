package ru.otus.homework;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.homework.service.ExaminationService;

public class Application {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

		ExaminationService examinationService = context
			.getBean("examinationServiceImpl", ExaminationService.class);
		examinationService.doExamination();

		context.close();
	}

}
