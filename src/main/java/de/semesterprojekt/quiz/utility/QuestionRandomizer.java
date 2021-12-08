package de.semesterprojekt.quiz.utility;

import de.semesterprojekt.quiz.entity.Question;
import de.semesterprojekt.quiz.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public Question[] getQuestions(int questionCount) {

        //Get a list of all available questions
        List<Question> questionList = questionRepository.findAll();

        //Set the max index value
        int maxIndex = questionList.size();

        //Create a question index
        int[] questionIndex = new int[questionCount];

        //Create a random number generator
        Random random = new Random();
        int randomNumber;
        boolean occupied;

        //Find only different numbers
        for(int i = 0; i < questionCount; i++) {

            do
            {
                //Resets the occupied status
                occupied = false;

                //Generate a new random number
                randomNumber = random.nextInt(maxIndex);

                //check if the number is occupied
                for (int j = 0; j < i; j++) {
                    if (randomNumber == questionIndex[j]) {
                        occupied = true;
                        break;
                    }
                }

                //sets the new number if it's not occupied
                if(!occupied) {
                    questionIndex[i] = randomNumber;
                }

            } while (occupied);
        }

        //Create an array for the questions
        Question[] questionSelection = new Question[questionCount];

        //get the questions (+1 -> db index start with 1)
        for(int i = 0; i < questionCount; i++) {
            questionSelection[i] = questionRepository.findById(questionIndex[i] + 1).get();
        }

        //Return the array
        return questionSelection;
    }
}