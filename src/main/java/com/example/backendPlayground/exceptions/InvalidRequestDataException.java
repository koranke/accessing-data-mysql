package com.example.backendPlayground.exceptions;

public class InvalidRequestDataException extends RuntimeException {
	public InvalidRequestDataException(String message) {
		super(message);
	}
}