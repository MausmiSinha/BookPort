package com.example.BookPort.auth.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
	
	public EmailAlreadyExistsException(String msg) {
		super(msg);
	}

}
