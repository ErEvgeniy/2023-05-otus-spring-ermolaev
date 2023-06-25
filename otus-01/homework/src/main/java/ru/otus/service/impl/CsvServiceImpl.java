package ru.otus.service.impl;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.core.io.Resource;
import ru.otus.exception.ResourceParsingException;
import ru.otus.service.CsvService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvServiceImpl implements CsvService {

	@Override
	public List<List<String>> parseCsvResource(Resource csvResource, char separator) {
		try (CSVReader reader = new CSVReader(new InputStreamReader(csvResource.getInputStream()))) {
			CSVParser parser = new CSVParserBuilder()
				.withSeparator(separator)
				.build();

			List<List<String>> rows = new ArrayList<>();

			for (String[] row : reader.readAll()) {
				String[] rowParts = parser.parseLine(row[0]);
				rows.add(Arrays.asList(rowParts));
			}

			return rows;
		} catch (IOException | CsvException ex) {
			throw new ResourceParsingException("Fail parsing csv resource", ex);
		}
	}

}
