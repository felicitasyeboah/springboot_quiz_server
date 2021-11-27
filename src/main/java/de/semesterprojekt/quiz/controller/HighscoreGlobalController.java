package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.HighscoreGlobal;
import de.semesterprojekt.quiz.repository.HighscoreGlobalRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The class controls the REST-mapping for the HighscoreGlobal-entity
 */
@RestController
@RequestMapping("/highscoreGlobal")
public class HighscoreGlobalController {

    private HighscoreGlobalRepository highscoreGlobalRepository;

    public HighscoreGlobalController(HighscoreGlobalRepository highscoreGlobalRepository) {
        this.highscoreGlobalRepository = highscoreGlobalRepository;
    }

    //Returns a list of the highscore entries
    @GetMapping("")
    public List<HighscoreGlobal> Index(){
        return highscoreGlobalRepository.findAll();
    }
}
