package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.homework.configuration.LocaleProvider;
import ru.otus.homework.service.MessageService;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

	private final MessageSource messageSource;

	private final LocaleProvider appProperties;

	@Override
	public String getMessage(String code) {
		return messageSource.getMessage(code, new String[]{}, appProperties.getLocale());
	}

	@Override
	public String getMessage(String code, Object[] params) {
		String[] stringParams = new String[params.length];
		for (int i = 0; i < params.length; i++) {
			stringParams[i] = String.valueOf(params[i]);
		}
		return messageSource.getMessage(code, stringParams, appProperties.getLocale());
	}

}
