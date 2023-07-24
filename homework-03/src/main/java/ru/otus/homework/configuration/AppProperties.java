package ru.otus.homework.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import ru.otus.homework.exception.UnexpectedLocaleException;

import java.util.Locale;

@Getter
@ConfigurationProperties(prefix = "application")
public class AppProperties implements LocaleProvider, QuestionProvider {

	private final Locale locale;

	private final String pathEN;

	private final String pathRU;

	private final char separator;

	@ConstructorBinding
	public AppProperties(Locale locale, String pathEN, String pathRU, char separator) {
		this.locale = locale;
		this.pathEN = pathEN;
		this.pathRU = pathRU;
		this.separator = separator;
	}

	public static class CsvResourceProvider implements ResourceProvider {

		private final ResourceLoader resourceLoader;

		private final AppProperties appProperties;

		public CsvResourceProvider(ResourceLoader resourceLoader, AppProperties appProperties) {
			this.resourceLoader = resourceLoader;
			this.appProperties = appProperties;
		}

		@Override
		public Resource getResource() {
			return switch (appProperties.getLocale().getLanguage()) {
				case "en" -> resourceLoader.getResource(appProperties.getPathEN());
				case "ru" -> resourceLoader.getResource(appProperties.getPathRU());
				default -> throw new UnexpectedLocaleException("Unexpected locale for questions");
			};
		}

	}

}
