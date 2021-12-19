package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.Question;
import de.semesterprojekt.quiz.repository.QuestionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * The class controls the REST-mapping for the Question-entity
 */
@CrossOrigin
@RestController
@RequestMapping("/question")
public class QuestionController {

    private QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Returns a list of all questions
     * @return List of questions
     */
    @GetMapping("")
    public List<Question> index(){

        return questionRepository.findAll();
    }

    /**
     * Returns a random question
     * @return question
     */
    @CrossOrigin
    @GetMapping("/random")
    public Question getRandom() {

        //Create a new random instance
        Random random = new Random();

        //Get all questions
        List<Question> questionList = index();

        //Generate a random index
        int randomNumber = random.nextInt(questionList.size());

        //Return the random Question object
        return questionList.get(randomNumber);
    }
}
