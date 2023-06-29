package ru.otus.service;

import org.springframework.core.io.Resource;

import java.util.List;

public interface CsvService {

	List<List<String>> parseCsvResource(Resource csvResource, char separator);

}
