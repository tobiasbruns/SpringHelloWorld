package de.com.bruns.sample.sampleapp.helloworld;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

@ExtendWith(MockitoExtension.class)
class HelloWorldServiceTest {

	@InjectMocks
	private HelloWorldService underTest;
	@Mock
	private HelloWorldRepository repository;

	@Test
	void findById() {
		HelloWorld testHello = HelloWorldTestFactory.create();
		when(repository.findById(any())).thenReturn(Optional.of(testHello));

		Optional<HelloWorld> loaded = underTest.findById(HelloWorldTestFactory.ID);

		assertThat(loaded).contains(testHello);
		verify(repository).findById(HelloWorldTestFactory.ID);
	}

	@Test
	void notFound() {
		when(repository.findById(any())).thenReturn(Optional.empty());

		Optional<HelloWorld> loaded = underTest.findById(HelloWorldTestFactory.ID);

		assertThat(loaded).isEmpty();
	}

	@Test
	void getAll() {
		when(repository.findAll()).thenReturn(Collections.emptyList());

		Stream<HelloWorld> all = underTest.getAll();

		assertThat(all).isEmpty();
		verify(repository).findAll();
	}

	@Test
	void create() {
		when(repository.save(any(HelloWorld.class))).then(i -> i.getArgument(0));

		HelloWorld testHello = HelloWorldTestFactory.create();
		HelloWorld saved = underTest.create(testHello);

		assertThat(saved).isNotNull();
		verify(repository).save(testHello);
	}

	@Nested
	@EmbeddedKafka
	class TestWithKafka {

		@Test
		void borkerAvailable(EmbeddedKafkaBroker broker) {
			assertThat(broker).isNotNull();
		}

	}

}
