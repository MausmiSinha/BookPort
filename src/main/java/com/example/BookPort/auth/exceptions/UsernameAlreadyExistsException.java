package com.example.BookPort.auth.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {

	public UsernameAlreadyExistsException(String msg) {
		super(msg);
	}
}
