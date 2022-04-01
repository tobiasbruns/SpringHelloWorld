package de.com.bruns.sample.sampleapp.helloworld;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.stream.Stream;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class HelloWorldModelAssembler implements RepresentationModelAssembler<HelloWorld, EntityModel<HelloWorld>> {

	@Override
	public EntityModel<HelloWorld> toModel(HelloWorld entity) {
		return EntityModel.of(entity, linkTo(HelloWorldController.class).slash(entity.getId()).withSelfRel());
	}

	public CollectionModel<EntityModel<HelloWorld>> toCollectionModel(Stream<HelloWorld> entities) {
		return CollectionModel.of(entities.map(this::toModel).toList(),
				linkTo(HelloWorldController.class).withSelfRel());
	}

}
