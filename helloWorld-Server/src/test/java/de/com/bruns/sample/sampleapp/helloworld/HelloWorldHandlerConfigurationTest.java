package de.com.bruns.sample.sampleapp.helloworld;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HelloWorldHandlerConfigurationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldDenieResource() throws Exception {
		mockMvc.perform(post("/hello/world").contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().is(422));
	}

	@Test
	void shouldDenieResource2() throws Exception {
		mockMvc.perform(post("/hello/world2").contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
				.andExpect(status().is(422));
	}

}
