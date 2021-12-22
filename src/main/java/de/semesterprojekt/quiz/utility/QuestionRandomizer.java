package de.semesterprojekt.quiz.utility;

import de.semesterprojekt.quiz.entity.Question;
import de.semesterprojekt.quiz.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The class is a generator of a question list
 */
@Component
public class QuestionRandomizer {

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * The method returns a random list of Question objects
     * @param questionCount
     * @return
     */
    public List<Question> getQuestions(int questionCount) {

        //Get a list of all available questions
        List<Question> questionList = questionRepository.findAll();

        //Shuffle all questions
        Collections.shuffle(questionList);

        //get the first questions out of the shuffled list
        questionList = questionList.subList(0,questionCount);

        //Return the list
        return questionList;
    }
}