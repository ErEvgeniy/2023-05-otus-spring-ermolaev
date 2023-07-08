package ru.otus.homework.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "questions")
public class QuestionProperties {

	private final String path;

	private final String separator;

	@ConstructorBinding
	public QuestionProperties(String path, String separator) {
		this.path = path;
		this.separator = separator;
	}

}
