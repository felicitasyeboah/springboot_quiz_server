package de.semesterprojekt.quiz;

import de.semesterprojekt.quiz.service.FilesStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class QuizApplication  {
/*public class QuizApplication implements CommandLineRunner {*/
	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {

		SpringApplication.run(QuizApplication.class, args);

		//TODO: A player is able to login twice with the same login data
	}

	//Uncomment to delete image folder

	/*@Override
	public void run(String... arg) throws Exception {
		storageService.deleteAll();
		storageService.init();
	}*/
}




