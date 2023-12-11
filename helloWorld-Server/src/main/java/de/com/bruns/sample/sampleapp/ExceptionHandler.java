package de.com.bruns.sample.sampleapp;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class ExceptionHandler extends AbstractHandlerExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		try {
			// if (ex instanceof ConstraintViolationException exception) {
			// response.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value());

			// return new ModelAndView();

			// } else {
			response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());

			return new ModelAndView();
			// }
		} catch (IOException e) {
			LOG.error("Error on sending http status error");
			return null;
		}

	}

}
