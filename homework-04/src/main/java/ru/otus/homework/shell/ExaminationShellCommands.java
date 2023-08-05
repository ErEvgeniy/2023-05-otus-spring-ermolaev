package ru.otus.homework.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.homework.domain.Student;
import ru.otus.homework.service.ExaminationService;
import ru.otus.homework.service.StudentService;

@ShellComponent
@RequiredArgsConstructor
public class ExaminationShellCommands extends AbstractShellComponent {

	private final ExaminationService examinationService;

	private final StudentService studentService;

	private Student student;

	@ShellMethod(value = "Login student", key = {"l", "login"})
	public void login() {
		this.student = studentService.greetingStudent();
	}

	@ShellMethod(value = "Start student examination", key = {"se", "start-examination"})
	@ShellMethodAvailability(value = "isStudentAuthorized")
	public void authorList() {
		examinationService.doExamination(student);
	}

	private Availability isStudentAuthorized() {
		return student == null ?
			Availability.unavailable("Пожалуйста представьтесь") : Availability.available();
	}

}
