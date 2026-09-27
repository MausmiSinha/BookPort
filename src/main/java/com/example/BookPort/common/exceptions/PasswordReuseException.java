package com.example.BookPort.common.exceptions;

public class PasswordReuseException extends RuntimeException{
	
	public PasswordReuseException(String message) {
        super(message);
    }

}
