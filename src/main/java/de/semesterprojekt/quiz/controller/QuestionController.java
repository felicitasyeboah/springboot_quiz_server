package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.Question;
import de.semesterprojekt.quiz.repository.QuestionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The class controls the REST-mapping for the Question-entity
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    private QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    //Returns a list of all questions
    @GetMapping("")
    public List<Question> Index(){
        return questionRepository.findAll();
    }
}
