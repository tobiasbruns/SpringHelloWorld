package de.com.bruns.sample.sampleapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class RootControllerTest {

	@InjectMocks
	private RootController controller;

	private MockMvc mockMvc;

	@BeforeEach
	void initTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	void getRoot() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}

}
