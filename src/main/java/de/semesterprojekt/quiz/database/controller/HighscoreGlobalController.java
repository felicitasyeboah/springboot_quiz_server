package de.semesterprojekt.quiz.database.controller;

import de.semesterprojekt.quiz.database.entity.HighscoreGlobal;
import de.semesterprojekt.quiz.database.repository.HighscoreGlobalRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The class controls the REST-mapping for the HighscoreGlobal-entity
 */
@CrossOrigin(origins = "http://localhost:4001")
@RestController
@RequestMapping("/highscoreGlobal")
public class HighscoreGlobalController {

    private HighscoreGlobalRepository highscoreGlobalRepository;

    public HighscoreGlobalController(HighscoreGlobalRepository highscoreGlobalRepository) {
        this.highscoreGlobalRepository = highscoreGlobalRepository;
    }

    /**
     * Returns the Highscore list
     * @return List of HighscoreGlobal
     */
    @GetMapping("")
    public List<HighscoreGlobal> index(){

        //TODO: Return top 5 highscores (define the count in gameConfig)
        return highscoreGlobalRepository.findAll();
    }
}
