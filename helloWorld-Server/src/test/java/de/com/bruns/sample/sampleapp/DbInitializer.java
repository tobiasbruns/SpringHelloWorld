package de.com.bruns.sample.sampleapp;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		MongoDBContainer mongoDbContainer = new MongoDBContainer(DockerImageName.parse("mongo").withTag("5"));
		LOG.info("Starting mongoDB ...");
		mongoDbContainer.start();
		LOG.info("mongoDB started.");

		TestPropertyValues
				.of("spring.data.mongodb.host=" + mongoDbContainer.getHost(),
						"spring.data.mongodb.port=" + mongoDbContainer.getFirstMappedPort())
				.applyTo(applicationContext.getEnvironment());

	}

}
