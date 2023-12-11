package de.com.bruns.sample.sampleapp.helloworld;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
// @Entity
@Document
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HelloWorld {

	public static final String RESOURCE_RELATION = "helloworld";

	@Id
	// @GeneratedValue
	@Setter(AccessLevel.PACKAGE)
	@JsonIgnore
	private String id;
	@NotNull
	@ApiModelProperty("nice greetings")
	private ComplexGreeting greeting;
	@ApiModelProperty("date of the greeting")
	private LocalDate theDate;

	@Builder
	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	static class ComplexGreeting {
		@ApiModelProperty("a friendly greeting")
		private String greeting;
		@ApiModelProperty("The addressee of the greeting")
		private String audience;
	}
}
