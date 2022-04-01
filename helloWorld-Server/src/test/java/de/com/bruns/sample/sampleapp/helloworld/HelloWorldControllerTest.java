package de.com.bruns.sample.sampleapp.helloworld;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.com.bruns.sample.sampleapp.TestUtils;
import io.swagger.annotations.ApiModelProperty;

@ExtendWith(MockitoExtension.class)
class HelloWorldControllerTest {

	@InjectMocks
	private HelloWorldController controller;
	@Mock
	private HelloWorldService service;
	@Mock
	private HelloWorldModelAssembler modelAssembler;

	@Captor
	private ArgumentCaptor<HelloWorld> helloWorldCaptor;

	private MockMvc mockMvc;

	@BeforeEach
	public void initTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	void getOne() throws Exception {
		when(service.findById(any())).thenReturn(Optional.of(HelloWorldTestFactory.create()));

		mockMvc.perform(get("/helloworlds/{id}", HelloWorldTestFactory.ID)).andExpect(status().isOk());

		verify(service).findById(HelloWorldTestFactory.ID);
	}

	@Test
	void notFound() throws Exception {
		when(service.findById(any())).thenReturn(Optional.empty());

		mockMvc.perform(get("/helloworlds/1")).andExpect(status().isNotFound());
	}

	@Test
	void getAll() throws Exception {
		when(service.getAll()).thenReturn(Stream.empty());

		mockMvc.perform(get("/helloworlds")).andExpect(status().isOk());

		verify(service).getAll();
	}

	@Test
	void add() throws Exception {
		when(service.create(any()))
				.then(i -> ((HelloWorld) i.getArgument(0)).toBuilder().id(HelloWorldTestFactory.ID).build());

		mockMvc.perform(post("/helloworlds").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.loadTextFile("requests/helloworld.json", ComplexGreetingTestFactory.AUDIENCE,
						ComplexGreetingTestFactory.GREETING, HelloWorldTestFactory.THE_DATE_STR))
				.accept(MediaType.APPLICATION_JSON))//
				.andExpect(status().isCreated());//

		verify(service).create(helloWorldCaptor.capture());
		assertThat(helloWorldCaptor.getValue()).usingRecursiveComparison()
				.isEqualTo(HelloWorldTestFactory.createBuilder().id(null).build());
	}

	@Test
	void edit() throws Exception {
		mockMvc.perform(put("/helloworlds/5").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.loadTextFile("requests/helloworld.json", ComplexGreetingTestFactory.AUDIENCE,
						ComplexGreetingTestFactory.GREETING, HelloWorldTestFactory.THE_DATE_STR)))
				.andExpect(status().isOk());

		verify(service).update(helloWorldCaptor.capture());

		assertThat(helloWorldCaptor.getValue()).usingRecursiveComparison()
				.isEqualTo(HelloWorldTestFactory.createBuilder().id("5").build());
	}

	@Test
	void remove() throws Exception {
		mockMvc.perform(delete("/helloworlds/{id}", HelloWorldTestFactory.ID)).andExpect(status().isNoContent());

		verify(service).delete(HelloWorldTestFactory.ID);
	}

	@Test
	void getApiDocDocs() {
		var fields = extractFields(HelloWorld.class);

		System.out.println(fields);
	}

	private FieldDescriptor[] extractFields(Class<?> clazz) {
		List<FieldDescriptor> descriptors = Arrays.stream(clazz.getFields())
				.filter(field -> field.getAnnotation(ApiModelProperty.class) != null)
				.map(field -> fieldWithPath(field.getName())
						.description(field.getAnnotation(ApiModelProperty.class).value()))
				.toList();

		return descriptors.toArray(new FieldDescriptor[descriptors.size()]);
	}
}