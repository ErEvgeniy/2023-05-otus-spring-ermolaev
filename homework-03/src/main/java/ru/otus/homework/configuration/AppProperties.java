package ru.otus.homework.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Locale;

@Getter
@ConfigurationProperties(prefix = "application")
public class AppProperties implements LocaleProvider {

	private final Locale locale;

	@ConstructorBinding
	public AppProperties(Locale locale) {
		this.locale = locale;
	}

}
