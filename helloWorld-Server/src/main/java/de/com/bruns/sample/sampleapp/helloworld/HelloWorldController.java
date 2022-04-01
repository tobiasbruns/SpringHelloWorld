package de.com.bruns.sample.sampleapp.helloworld;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.com.bruns.sample.sampleapp.ResourceNotFoundException;

@RestController
@RequestMapping(path = "/helloworlds")
public class HelloWorldController {

	@Autowired
	private HelloWorldService service;

	@Autowired
	private HelloWorldModelAssembler entityModelAssembler;

	@GetMapping
	public CollectionModel<EntityModel<HelloWorld>> getAll() {
		return entityModelAssembler.toCollectionModel(service.getAll());
	}

	@GetMapping("/{helloworldId}")
	public EntityModel<HelloWorld> getOne(@PathVariable String helloworldId) {
		return entityModelAssembler.toModel(service.findById(helloworldId).orElseThrow(ResourceNotFoundException::new));
	}

	@PostMapping
	public ResponseEntity<Void> create(@RequestBody HelloWorld helloWorld) {
		var saved = service.create(helloWorld);
		return ResponseEntity.created(linkTo(HelloWorldController.class).slash(saved.getId()).toUri()).build();
	}

	@PutMapping("/{helloworldId}")
	public EntityModel<HelloWorld> edit(@PathVariable String helloworldId, @RequestBody HelloWorld helloWorld) {
		helloWorld.setId(helloworldId);
		return entityModelAssembler.toModel(service.update(helloWorld));
	}

	@DeleteMapping("/{helloworldId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remove(@PathVariable String helloworldId) {
		service.delete(helloworldId);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Void> handleNotFound() {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
