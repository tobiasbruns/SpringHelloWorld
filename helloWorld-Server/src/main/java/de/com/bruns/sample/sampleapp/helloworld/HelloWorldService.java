package de.com.bruns.sample.sampleapp.helloworld;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelloWorldService {

	@Autowired
	private HelloWorldRepository repository;

	public Stream<HelloWorld> getAll() {
		return StreamSupport.stream(repository.findAll().spliterator(), false);
	}

	public Optional<HelloWorld> findById(String id) {
		return repository.findById(id);
	}

	public HelloWorld create(HelloWorld helloWorld) {
		return repository.save(helloWorld);
	}

	public HelloWorld update(HelloWorld helloWorld) {
		return repository.save(helloWorld);
	}

	public void delete(String id) {
		repository.deleteById(id);
	}
}
