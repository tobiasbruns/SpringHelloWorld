package de.com.bruns.sample.sampleapp.helloworld;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import de.com.bruns.sample.sampleapp.callscope.CallScopeBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class HelloWorldService {

	@Autowired
	private HelloWorldRepository repository;
	@Autowired
	private CallScopeBean scopeBean;

	public Stream<HelloWorld> getAll() {
		return StreamSupport.stream(repository.findAll().spliterator(), false);
	}

	public Optional<HelloWorld> findById(String id) {
		return repository.findById(id);
	}

	public HelloWorld create(@Valid HelloWorld helloWorld) {
		return repository.save(helloWorld);
	}

	public HelloWorld update(HelloWorld helloWorld) {
		return repository.save(helloWorld);
	}

	@Async
	public void asyncCreating() {
		LOG.info("running async! " + scopeBean.getId());
		repository.findById("42");
	}

	public void delete(String id) {
		repository.deleteById(id);
	}
}
