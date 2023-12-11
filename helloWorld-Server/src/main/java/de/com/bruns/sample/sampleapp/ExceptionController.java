package de.com.bruns.sample.sampleapp;

import jakarta.validation.ConstraintViolationException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.function.ServerResponse;

@RestControllerAdvice
public class ExceptionController {

	@ExceptionHandler(ConstraintViolationException.class)
	public ServerResponse handleConstraintViolationException(ConstraintViolationException e) {
		return ServerResponse.unprocessableEntity().body("violation");
	}

}
