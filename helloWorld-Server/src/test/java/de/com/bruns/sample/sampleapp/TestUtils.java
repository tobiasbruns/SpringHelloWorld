package de.com.bruns.sample.sampleapp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class TestUtils {

	public static final String RESTDOC_BASE_FOLDER = "../target/generated-snippets";

	private TestUtils() {
	}

	public static InputStream loadFile(final String fileName) {
		InputStream stream = TestUtils.class.getClassLoader().getResourceAsStream(fileName);
		if (stream == null) {
			throw new RuntimeException("File '" + fileName + "' not found."); // NOSONAR
		}
		return stream;
	}

	public static String loadTextFile(final String fileName) {
		try {
			return IOUtils.toString(loadFile(fileName), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e); // NOSONAR
		}
	}

	public static String loadTextFile(final String fileName, final String... args) {
		return String.format(loadTextFile(fileName), (Object[]) args);
	}

}
