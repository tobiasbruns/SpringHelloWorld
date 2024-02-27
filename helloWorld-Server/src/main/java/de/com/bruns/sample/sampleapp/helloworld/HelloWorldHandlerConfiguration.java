package de.com.bruns.sample.sampleapp.helloworld;

import static org.springframework.web.servlet.function.RouterFunctions.route;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import de.com.bruns.sample.sampleapp.ExceptionController;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.java.Log;

@Log
@Configuration
public class HelloWorldHandlerConfiguration {

	@Autowired
	private ExceptionController exceptionController;

	@Autowired
	private HelloWorldHandler handler;
	@Autowired
	private Validator validator;

	@Autowired
	private ObservationRegistry registry;

	@Bean
	RouterFunction<ServerResponse> helloWorldRouter() {
		var observation = createObservation();

		return route()
				.before(request -> startObservation(request, observation))
				.before(this::logRequest)
				.POST("/hello/world", req -> handler.handlePost(req.body(HelloWorld.class)))
				.GET("/hello/world", req -> handler.handleGet())

				.onError(ConstraintViolationException.class,
						(exc, req) -> exceptionController.handleConstraintViolationException((ConstraintViolationException) exc))
				.after(this::logResult)
				.after((request, response) -> stopObservation(response, observation))
				.build();

	}

	private Observation createObservation() {
		return Observation.createNotStarted("user.name", registry)
				.contextualName("klaus");
	}

	private ServerRequest startObservation(ServerRequest request, Observation observation) {
		observation.start();
		return request;
	}

	private ServerResponse stopObservation(ServerResponse response, Observation observation) {
		observation.stop();
		return response;
	}

	@Bean
	RouterFunction<ServerResponse> helloWorld2Router(Validator validator, HelloWorldService service) {
		return route()
				.POST("/hello/world2", req -> process(req, HelloWorld.class, service::create))
				.GET("/hello/world2", req -> process(service::getAll))
				.onError(ConstraintViolationException.class,
						(exc, req) -> exceptionController.handleConstraintViolationException((ConstraintViolationException) exc))
				.build();
	}

	private <A, B> ServerResponse process(ServerRequest req, Class<A> entityClass, Function<A, B> processor)
			throws ServletException, IOException {
		A body = req.body(entityClass);
		validate(body);
		return ServerResponse.ok().body(processor.apply(body));
	}

	private <B> ServerResponse process(Supplier<B> supplier) {
		var result = supplier.get();
		return ServerResponse.ok().body(result);
	}

	private void validate(Object bodyObject) {
		var result = validator.validate(bodyObject);
		if (!result.isEmpty()) {
			throw new ConstraintViolationException(result);
		}
	}

	private ServerRequest logRequest(ServerRequest request) {
		LOG.info(request.uri().toString());
		return request;
	}

	private ServerResponse logResult(ServerRequest request, ServerResponse response) {
		LOG.info("Result: " + response.statusCode());
		return response;
	}

}
