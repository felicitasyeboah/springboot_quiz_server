package de.semesterprojekt.quiz.utility;

import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.database.entity.Question;
import de.semesterprojekt.quiz.database.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * The class is a generator of a question list
 */
@Component
public class QuestionRandomizer {

    @Autowired
    private QuestionRepository questionRepository;

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