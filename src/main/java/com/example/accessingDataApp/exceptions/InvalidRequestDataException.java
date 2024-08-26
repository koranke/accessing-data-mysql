package com.example.accessingDataApp.exceptions;

public class InvalidRequestDataException extends RuntimeException {
	public InvalidRequestDataException(String message) {
		super(message);
	}
}