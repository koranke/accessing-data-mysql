package com.example.backendPlayground.exceptions;

public class InvalidEnumValueException extends RuntimeException {
	public InvalidEnumValueException(String message) {
		super(message);
	}
}