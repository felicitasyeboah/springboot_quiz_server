package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.Question;
import de.semesterprojekt.quiz.repository.QuestionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private QuestionRepository questionRepository;

    public QuestionController(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping("")
    public List<Question> Index(){
        return questionRepository.findAll();
    }
}
