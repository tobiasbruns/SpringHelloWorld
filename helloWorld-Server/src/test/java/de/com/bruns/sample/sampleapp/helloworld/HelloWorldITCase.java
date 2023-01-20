package de.com.bruns.sample.sampleapp.helloworld;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import de.com.bruns.sample.sampleapp.DataITCase;
import de.com.bruns.sample.sampleapp.TestUtils;

@DataITCase
class HelloWorldITCase {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MongoTemplate mongoTemplate;

	@SpyBean
	private HelloWorldRepository repository;

	@BeforeEach
	void clearDB() {
		mongoTemplate.dropCollection(HelloWorld.class);
	}

	@Test
	void getAll() throws Exception {
		mongoTemplate.save(HelloWorldTestFactory.createBuilder().id(null).build());

		mockMvc.perform(get("/helloworlds")).andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.helloWorldList.length()").value(1)).andExpect(
						jsonPath("$._embedded.helloWorldList[0].greeting.greeting").value(ComplexGreetingTestFactory.GREETING));
	}

	@Test
	void shouldCallAsyncRepo() throws Exception {
		MockUtil.resetMock(repository);
		mongoTemplate.save(HelloWorldTestFactory.createBuilder().id(null).build());

		mockMvc.perform(get("/helloworlds"));

		verify(repository, timeout(1000)).findById("42");
	}

	@Test
	void create() throws Exception {
		mockMvc.perform(post("/helloworlds")//
				.contentType(MediaType.APPLICATION_JSON)//
				.content(//
						TestUtils.loadTextFile("requests/helloworld.json", //
								ComplexGreetingTestFactory.AUDIENCE, ComplexGreetingTestFactory.GREETING,
								HelloWorldTestFactory.THE_DATE_STR)))
				.andExpect(status().isCreated());

		var loaded = mongoTemplate.findAll(HelloWorld.class);

		assertThat(loaded.get(0)).usingRecursiveComparison().ignoringFields("id")
				.isEqualTo(HelloWorldTestFactory.create());
	}
}
