package ru.otus;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.service.ExaminationService;

public class Application {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

		ExaminationService examinationService = context
			.getBean("examinationServiceImpl", ExaminationService.class);
		examinationService.doExamination();

		context.close();
	}

}
