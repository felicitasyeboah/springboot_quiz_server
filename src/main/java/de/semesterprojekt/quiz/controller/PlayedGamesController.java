package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.PlayedGames;
import de.semesterprojekt.quiz.repository.PlayedGamesRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The class controls the REST-mapping for the PlayedGames-entity
 */
@RestController
@RequestMapping("/playedGames")
public class PlayedGamesController {

    private PlayedGamesRepository playedGamesRepository;

    public PlayedGamesController(PlayedGamesRepository playedGamesRepository) {
        this.playedGamesRepository = playedGamesRepository;
    }

    //Returns a list of all played games
    @GetMapping("")
    public List<PlayedGames> Index(){
        return playedGamesRepository.findAll();
    }
}
