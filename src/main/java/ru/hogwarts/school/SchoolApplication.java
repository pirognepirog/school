package ru.hogwarts.school;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class SchoolApplication {
    // http://localhost:8080/webjars/swagger-ui/index.html - не работает
     // http://localhost:8080/swagger-ui/index.html
	public static void main(String[] args) {

        SpringApplication.run(SchoolApplication.class, args);
	}

}
