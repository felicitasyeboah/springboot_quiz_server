package de.semesterprojekt.quiz.database.controller;

import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.database.entity.Question;
import de.semesterprojekt.quiz.database.repository.QuestionRepository;
import org.springframework.stereotype.Controller;
import java.util.Collections;
import java.util.List;

/**
 * The class controls the methods for the Question-entity
 */
@Controller
public class QuestionController {

    private QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {

        this.questionRepository = questionRepository;
    }

    /**
     * Returns a list of all questions
     * @return List of questions
     */
    private List<Question> index(){

        return questionRepository.findAll();
    }

    /**
     * The method returns a random list of Question objects
     * @return a list of questions
     */
    public List<Question> getQuestions() {

        //Get a list of all available questions
        List<Question> questionList = index();

        //Shuffle all questions
        Collections.shuffle(questionList);

        //get the first questions out of the shuffled list
        questionList = questionList.subList(0, GameConfig.COUNT_QUESTION);

        //Return the list
        return questionList;
    }
}
