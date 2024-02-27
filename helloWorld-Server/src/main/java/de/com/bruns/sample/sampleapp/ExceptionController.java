package de.com.bruns.sample.sampleapp;

import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.function.ServerResponse;

import io.micrometer.observation.ObservationRegistry;

@RestControllerAdvice
public class ExceptionController {

	@Autowired
	private ObservationRegistry registry;

	@ExceptionHandler(ConstraintViolationException.class)
	public ServerResponse handleConstraintViolationException(ConstraintViolationException e) {
		registry.getCurrentObservation().error(e);
		return ServerResponse.unprocessableEntity().body("violation");
	}

}
