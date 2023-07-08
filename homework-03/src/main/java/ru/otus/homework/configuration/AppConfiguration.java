package ru.otus.homework.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.homework.service.IOService;
import ru.otus.homework.service.impl.IOServiceStreams;
import ru.otus.homework.util.ExtendedResourceBundleMessageSource;

@Configuration
@EnableConfigurationProperties({AppProperties.class, QuestionProperties.class})
public class AppConfiguration {

	@Bean
	public IOService iOServiceStreams() {
		return new IOServiceStreams(System.out, System.in);
	}

	@Bean
	public ExtendedResourceBundleMessageSource extendedResourceBundleMessageSource() {
		return new ExtendedResourceBundleMessageSource();
	}

}
