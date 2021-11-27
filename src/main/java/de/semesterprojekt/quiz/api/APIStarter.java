package de.semesterprojekt.quiz.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The class starts the API documentation.
 */
@OpenAPIDefinition(info = @Info(title = "Quizserver", version = "1.0", description = "Dokumentation der API"))
@SpringBootApplication
public class APIStarter {

}