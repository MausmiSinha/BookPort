package com.example.BookPort.common.aspects;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.BookPort.auth.exceptions.EmailAlreadyExistsException;
import com.example.BookPort.auth.exceptions.PasswordMismatchExceptio;
import com.example.BookPort.auth.exceptions.PasswordReuseException;
import com.example.BookPort.auth.exceptions.RegistrationRequestNotFoundException;
import com.example.BookPort.auth.exceptions.RoleNotFoundException;
import com.example.BookPort.auth.exceptions.UserNotFoundException;
import com.example.BookPort.auth.exceptions.UsernameAlreadyExistsException;
import com.example.BookPort.auth.exceptions.ValidationException;
import com.example.BookPort.common.dto.ErrorDetailsDto;
import com.example.BookPort.common.logging.Debugger;
import com.example.BookPort.common.util.BookPortResponseBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class RestExceptionsHandler {

	private Debugger d = new Debugger(this.getClass());

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request) {
		d.dbg("Something went wrong: ", Level.ERROR, ex);
		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<Object> handleDisabledException(DisabledException ex, HttpServletRequest request) {
		d.dbg("Something went wrong: ", Level.ERROR, ex);
		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse("BKL-AUT-403", ex.getMessage()),
				HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException ex,
			HttpServletRequest request) {
		d.dbg("Something went wrong: ", Level.ERROR, ex);
		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse("BKL-AUT-401", ex.getMessage()),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UsernameAlreadyExistsException.class)
	public ResponseEntity<Object> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex,
			HttpServletRequest request) {
		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse("BKL-ERR-400", ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		d.dbg("Inside handleMethodArgumentNotValidException");
		List<ErrorDetailsDto> errors = new ArrayList<ErrorDetailsDto>();
		for (FieldError fe : ex.getFieldErrors()) {
			d.dbg(fe.getDefaultMessage());
			d.dbg(fe.getDefaultMessage());
			d.dbg(fe.getField());
			errors.add(new ErrorDetailsDto("BKL-VAL-001", fe.getDefaultMessage(), fe.getField()));
		}
		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse(errors), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
			HttpServletRequest request) {
		d.dbg("Inside handleConstraintViolationException");
		List<ErrorDetailsDto> errors = new ArrayList<ErrorDetailsDto>();
		for (ConstraintViolation<?> fe : ex.getConstraintViolations()) {
			d.dbg(fe.getMessage());
			d.dbg("Property Path: " + fe.getPropertyPath());
			errors.add(new ErrorDetailsDto("BKL-VAL-001", fe.getMessage(), fe.getPropertyPath().toString()));
		}
		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse(errors), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<Object> handleValidationException(ValidationException ex, HttpServletRequest request) {
		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse("BKL-VAL-400", ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex,
			HttpServletRequest request) {
		d.dbg("Something went wrong: ", Level.ERROR, ex);
		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse("BKL-AUT-401", ex.getMessage()),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
			HttpServletRequest request) {
		d.dbg("Something went wrong: ", Level.ERROR, ex);
		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse("BKL-RES-404", ex.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex,
			HttpServletRequest request) {
		return new ResponseEntity<Object>(
				BookPortResponseBuilder.buildFailureResponse("BKL-ERR-400", "Email already taken."),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RoleNotFoundException.class)
	public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException ex, HttpServletRequest request) {

		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse("BKL-RES-404", ex.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {

		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse("BKL-USR-404", ex.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PasswordMismatchExceptio.class)
	public ResponseEntity<Object> handlePasswordMismatchException(PasswordMismatchExceptio ex,
			HttpServletRequest request) {

		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse("BKL-PWD-400", ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PasswordReuseException.class)
	public ResponseEntity<Object> handlePasswordReuseException(PasswordReuseException ex, HttpServletRequest request) {

		return new ResponseEntity<Object>(BookPortResponseBuilder.buildFailureResponse("BKL-PWD-409", ex.getMessage()),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(RegistrationRequestNotFoundException.class)
	public ResponseEntity<Object> handleRegistrationRequestNotFoundException(RegistrationRequestNotFoundException ex,
			HttpServletRequest request) {
		return new ResponseEntity<>(BookPortResponseBuilder.buildFailureResponse("BKL-REG-404", ex.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingServletRequestParameterException(
			MissingServletRequestParameterException ex, HttpServletRequest request) {
		return new ResponseEntity<>(BookPortResponseBuilder.buildFailureResponse("BKL-ERR-400", ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}
}
