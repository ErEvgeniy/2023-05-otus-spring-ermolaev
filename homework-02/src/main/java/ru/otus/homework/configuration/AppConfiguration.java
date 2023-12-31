package ru.otus.homework.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import ru.otus.homework.service.IOService;
import ru.otus.homework.service.impl.IOServiceStreams;

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class AppConfiguration {

	@Bean
	public IOService iOServiceStreams() {
		return new IOServiceStreams(System.out, System.in);
	}

	@Bean
	public ResourceLoader defaultResourceLoader() {
		return new DefaultResourceLoader();
	}

}
