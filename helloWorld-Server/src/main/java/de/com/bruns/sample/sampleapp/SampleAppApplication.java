package de.com.bruns.sample.sampleapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.filter.ForwardedHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootApplication
public class SampleAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleAppApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
		FilterRegistrationBean<ForwardedHeaderFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new ForwardedHeaderFilter());
		return bean;
	}

	@Bean
	@Primary
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		return builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
		// return builder.simpleDateFormat("yyyy")
		// .dateFormat(StdDateFormat.getISO8601Format(TimeZone.getDefault(),
		// Locale.GERMANY)).build();
	}
}
