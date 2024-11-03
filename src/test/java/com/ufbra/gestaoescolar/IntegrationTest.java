package com.ufbra.gestaoescolar;

import com.ufbra.gestaoescolar.config.AsyncSyncConfiguration;
import com.ufbra.gestaoescolar.config.EmbeddedSQL;
import com.ufbra.gestaoescolar.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { GestaoEscolarApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
