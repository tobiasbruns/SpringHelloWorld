package de.com.bruns.sample.sampleapp.helloworld;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.com.bruns.sample.sampleapp.AnnotationBasedFieldDocumentator;
import de.com.bruns.sample.sampleapp.TestUtils;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
// @AutoConfigureMockMvc
class HelloWorldControllerITCase {

	// @Autowired
	private MockMvc mockMvc;
	@MockBean
	private HelloWorldService service;

	@BeforeEach
	void setupMvc(WebApplicationContext ctxt, RestDocumentationContextProvider docContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(ctxt)
				.apply(documentationConfiguration(docContext).uris().withHost("bruns.com.de").withPort(80).and()
						.operationPreprocessors().withResponseDefaults(prettyPrint()))

				.build();
	}

	@Test
	void getOne() throws Exception {
		when(service.findById(any())).thenReturn(Optional.of(HelloWorldTestFactory.create()));

		mockMvc.perform(get("/helloworlds/5")).andExpect(status().isOk())//
				.andExpect(jsonPath("$.greeting.greeting").value(ComplexGreetingTestFactory.GREETING))//
				.andExpect(jsonPath("$.theDate").value("2007-05-03"))//
				.andExpect(jsonPath("$._links.self.href")
						.value("http://bruns.com.de/helloworlds/" + HelloWorldTestFactory.ID))
				.andDo(document("getOne",
						responseFields(AnnotationBasedFieldDocumentator.extractFieldsIngoreLinks(HelloWorld.class))));
	}

	@Test
	void create() throws Exception {
		when(service.create(any(HelloWorld.class))).thenReturn(HelloWorldTestFactory.create());

		mockMvc.perform(post("/helloworlds").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.loadTextFile("requests/helloworld.json", ComplexGreetingTestFactory.AUDIENCE,
						ComplexGreetingTestFactory.GREETING, HelloWorldTestFactory.THE_DATE_STR)))
				.andExpect(status().isCreated())//
				.andExpect(header().string("Location", "http://bruns.com.de/helloworlds/" + HelloWorldTestFactory.ID))
				.andDo(document("create",
						requestFields(AnnotationBasedFieldDocumentator.extractFields(HelloWorld.class))));
	}

}
