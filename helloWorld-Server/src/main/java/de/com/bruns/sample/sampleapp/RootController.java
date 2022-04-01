package de.com.bruns.sample.sampleapp;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.com.bruns.sample.sampleapp.helloworld.HelloWorld;
import de.com.bruns.sample.sampleapp.helloworld.HelloWorldController;

@RestController
@RequestMapping
public class RootController {

	@Autowired(required = false)
	public BuildProperties buildProperties;

	@GetMapping
	public RootResource getRootResource() {
		return new RootResource(//
				linkTo(RootController.class).withSelfRel(), //
				linkTo(HelloWorldController.class).withRel(HelloWorld.RESOURCE_RELATION));
	}

	class RootResource extends RepresentationModel<RootResource> {

		public RootResource(Link... links) {
			add(links);
		}

		public String getVersion() {
			return buildProperties == null ? "--" : buildProperties.getVersion();
		}

		public Instant getBuildTime() {
			return buildProperties == null ? null : buildProperties.getTime();
		}
	}
}
