package de.com.bruns.sample.sampleapp.helloworld;

import java.time.LocalDate;
import java.util.UUID;

public class HelloWorldTestFactory {

	static final String ID = UUID.randomUUID().toString();
	static final String THE_DATE_STR = "2007-05-03";
	static final LocalDate THE_DATE = LocalDate.parse(THE_DATE_STR);

	static HelloWorld create() {
		return createBuilder().build();
	}

	static HelloWorld.HelloWorldBuilder createBuilder() {
		return HelloWorld.builder() //
				.id(ID) //
				.greeting(ComplexGreetingTestFactory.create())//
				.theDate(THE_DATE);
	}
}
