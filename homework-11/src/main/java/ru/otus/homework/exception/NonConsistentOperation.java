package ru.otus.homework.exception;

public class NonConsistentOperation extends RuntimeException {

	public NonConsistentOperation(String message) {
		super(message);
	}

}
