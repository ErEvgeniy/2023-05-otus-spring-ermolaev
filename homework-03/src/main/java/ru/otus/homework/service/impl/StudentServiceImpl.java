package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.constant.MessageCode;
import ru.otus.homework.domain.Student;
import ru.otus.homework.service.IOService;
import ru.otus.homework.service.MessageService;
import ru.otus.homework.service.StudentService;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	private final IOService ioService;

	private final MessageService messageService;

	@Override
	public Student greetingStudent() {
		Student student = new Student();

		String askFirstnameMessage = messageService.getMessage(MessageCode.EXAM_ASK_FIRSTNAME);
		student.setFirstname(ioService.readStringWithPrompt(askFirstnameMessage));

		String askLastnameMessage = messageService.getMessage(MessageCode.EXAM_ASK_LASTNAME);
		student.setLastname(ioService.readStringWithPrompt(askLastnameMessage));

		return student;
	}

}
