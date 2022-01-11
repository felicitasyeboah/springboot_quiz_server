package de.semesterprojekt.quiz.database.controller;

import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.database.entity.Question;
import de.semesterprojekt.quiz.database.repository.QuestionRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
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
    @GetMapping("/all")
    public List<Question> index(){

        return questionRepository.findAll();
    }

    /**
     * Returns a random question
     * @return question
     */

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

    /**
     * The method returns a random list of Question objects
     * @return a list of questions
     */
    public List<Question> getQuestions() {

        //Get a list of all available questions
        List<Question> questionList = questionRepository.findAll();

        //Shuffle all questions
        Collections.shuffle(questionList);

        //get the first questions out of the shuffled list
        questionList = questionList.subList(0, GameConfig.COUNT_QUESTION);

        //Return the list
        return questionList;
    }
}
