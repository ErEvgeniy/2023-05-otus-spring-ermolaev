package ru.otus.homework;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.homework.service.ExaminationService;

@ComponentScan()
public class Application {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

		ExaminationService examinationService = context
			.getBean("examinationServiceImpl", ExaminationService.class);
		examinationService.doExamination();
	}

}
