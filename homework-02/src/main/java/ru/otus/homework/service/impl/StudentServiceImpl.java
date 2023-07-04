package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Student;
import ru.otus.homework.service.IOService;
import ru.otus.homework.service.StudentService;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	private final IOService ioService;

	@Override
	public Student greetingStudent() {
		Student student = new Student();
		student.setFirstname(ioService.readStringWithPrompt("Please enter your firstname"));
		student.setLastname(ioService.readStringWithPrompt("Please enter your lastname"));
		return student;
	}

}
