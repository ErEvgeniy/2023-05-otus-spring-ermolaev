package ru.otus.homework.exception;

public class ResourceParsingException extends RuntimeException {

	public ResourceParsingException(String message, Exception ex) {
		super(message, ex);
	}

}
