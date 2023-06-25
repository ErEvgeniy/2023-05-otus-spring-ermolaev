package ru.otus.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AppConfig {
	
	private final String questionResourcePath;

	private final char questionSeparator;

}
