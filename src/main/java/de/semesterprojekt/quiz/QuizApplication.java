/* Semesterprojekt 2021/22
		Gruppe B */

package de.semesterprojekt.quiz;
import de.semesterprojekt.quiz.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuizApplication {

	public static void main(String[] args) {

		SpringApplication.run(QuizApplication.class, args);

	}
}
