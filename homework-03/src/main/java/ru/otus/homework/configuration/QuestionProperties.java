package ru.otus.homework.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "questions")
public class QuestionProperties implements QuestionProvider {

	private final String pathEN;

	private final String pathRU;

	private final char separator;

	@ConstructorBinding
	public QuestionProperties(String pathEN, String pathRU, char separator) {
		this.pathEN = pathEN;
		this.pathRU = pathRU;
		this.separator = separator;
	}

}
