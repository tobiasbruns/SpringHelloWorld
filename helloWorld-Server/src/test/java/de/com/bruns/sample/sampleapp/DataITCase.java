package de.com.bruns.sample.sampleapp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited

@ITCase
@ContextConfiguration(initializers = { DbInitializer.class })
@ActiveProfiles({ "itcase", "with_db" })
public @interface DataITCase {

}
