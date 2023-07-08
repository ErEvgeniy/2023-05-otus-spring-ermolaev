package ru.otus.homework.util;

import org.springframework.context.support.ResourceBundleMessageSource;
import ru.otus.homework.exception.ResourceParsingException;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ExtendedResourceBundleMessageSource extends ResourceBundleMessageSource {

	public Map<String, String> getBundleMessages(String bundleName, Locale locale) {
		super.setBasename(bundleName);

		ResourceBundle bundle = getResourceBundle(bundleName, locale);
		if (bundle == null) {
			throw new ResourceParsingException(
				String.format("Bundle with name '%s' not found", bundleName));
		}

		Map<String, String> messageMap = new LinkedHashMap<>();
		for (String key : bundle.keySet()) {
			messageMap.put(key, getMessage(key, new String[]{}, locale));
		}
		return messageMap;
	}

}
