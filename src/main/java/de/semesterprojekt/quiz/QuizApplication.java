package de.semesterprojekt.quiz;

import de.semesterprojekt.quiz.fileservice.storageservice.FilesStorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.Resource;

@SpringBootApplication
public class QuizApplication  {

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {

		SpringApplication.run(QuizApplication.class, args);

		System.out.println("Server started successfully.");
	}
}