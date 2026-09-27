package com.example.BookPort.common.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {

	public UsernameAlreadyExistsException(String msg) {
		super(msg);
	}
}
