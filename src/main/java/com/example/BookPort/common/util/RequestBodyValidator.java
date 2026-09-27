package com.example.BookPort.common.util;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Component
public class RequestBodyValidator {
	
	@Autowired
	private Validator validator;
	
	public <T> void validateDto(T request) {
		Set<ConstraintViolation<T>> voilations = validator.validate(request);
	    
	    if(!voilations.isEmpty()) {
	    	throw new ConstraintViolationException(voilations);
	    }
	}

}
