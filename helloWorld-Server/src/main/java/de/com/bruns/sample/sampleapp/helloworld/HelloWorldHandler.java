package de.com.bruns.sample.sampleapp.helloworld;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.function.ServerResponse;

@Component
@Validated
public class HelloWorldHandler {

	@Autowired
	private HelloWorldService service;

	ServerResponse handlePost(@Valid HelloWorld helloWorld) {
		var created = service.create(helloWorld);
		return ServerResponse.accepted().body(created);
	}

	ServerResponse handleGet() {
		return ServerResponse.ok().body(service.getAll().toList());
	}
}
