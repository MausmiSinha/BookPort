package com.example.BookPort.common.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
	
	public EmailAlreadyExistsException(String msg) {
		super(msg);
	}

}
