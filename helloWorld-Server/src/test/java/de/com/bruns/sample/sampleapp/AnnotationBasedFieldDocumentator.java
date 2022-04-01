package de.com.bruns.sample.sampleapp;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.restdocs.payload.FieldDescriptor;

import io.swagger.annotations.ApiModelProperty;

public class AnnotationBasedFieldDocumentator {

	public static List<FieldDescriptor> extractFields(Class<?> clazz) {
		return extractFields(clazz, Optional.empty());
	}

	public static List<FieldDescriptor> extractFieldsIngoreLinks(Class<?> clazz) {
		var fieldDescriptions = extractFields(clazz, Optional.empty());
		var result = new ArrayList<>(fieldDescriptions);
		result.add(subsectionWithPath("_links").ignored());
		return result;
	}

	private static List<FieldDescriptor> extractFields(Class<?> clazz, Optional<String> path) {
		return Arrays.stream(clazz.getDeclaredFields())
				.filter(field -> field.getAnnotation(ApiModelProperty.class) != null)
				.map(field -> fromAnnotatedField(field, path))
				.flatMap(List::stream).toList();
	}

	private static List<FieldDescriptor> fromAnnotatedField(Field field, Optional<String> path) {
		if (field.getType().isPrimitive() || !field.getType().getPackage().getName().startsWith("de.com.bruns")) {

			return Collections.singletonList(
					fieldWithPath(addPath(path, field.getName()))
							.description(field.getAnnotation(ApiModelProperty.class).value()));
		}
		return extractFields(field.getType(), Optional.of(addPath(path, field.getName())));
	}

	private static String addPath(Optional<String> path, String name) {
		return path.map(p -> p + "." + name).orElse(name);
	}
}
