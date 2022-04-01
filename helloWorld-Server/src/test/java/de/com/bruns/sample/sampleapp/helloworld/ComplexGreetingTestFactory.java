package de.com.bruns.sample.sampleapp.helloworld;

import de.com.bruns.sample.sampleapp.helloworld.HelloWorld.ComplexGreeting;

class ComplexGreetingTestFactory {
	static final String GREETING = "HELLO";
	static final String AUDIENCE = "WORLD";

	static ComplexGreeting create() {
		return createBuilder().build();
	}

	static ComplexGreeting.ComplexGreetingBuilder createBuilder() {
		return ComplexGreeting.builder()//
				.greeting(GREETING)//
				.audience(AUDIENCE);
	}
}
