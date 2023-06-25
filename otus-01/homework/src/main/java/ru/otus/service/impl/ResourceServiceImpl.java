package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import ru.otus.service.ResourceService;

@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

	private final ResourceLoader resourceLoader;

	public Resource getResource(String resourcePath) {
		return resourceLoader.getResource(resourcePath);
	}

}
