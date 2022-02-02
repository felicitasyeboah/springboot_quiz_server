package de.semesterprojekt.quiz.database.controller;

import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.database.entity.PlayedGame;
import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.database.model.*;
import de.semesterprojekt.quiz.database.repository.PlayedGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * The class controls the REST-mapping and offers a public method for the PlayedGames-entity
 */
@CrossOrigin
@RestController
public class PlayedGameController {

    @Autowired
    private PlayedGameRepository playedGameRepository;

    @Autowired
    private UserController userController;

    public PlayedGameController(PlayedGameRepository playedGameRepository) {
        this.playedGameRepository = playedGameRepository;
    }

    /**
     * index method that returns all played games
     * @return
     */
    private List<PlayedGame> index() {
        return playedGameRepository.findAll();
    }

    /**
     * Returns the highscore list
     */
    @GetMapping(path = "/highscore")
    public ResponseEntity<List<HighscoreEntry>> getHighScore() {

        //Get all played games
        List<PlayedGame> playedGameList = index();
        List<HighscoreEntry> highscoreList = new ArrayList<>();

        if(playedGameList.size() > 0) {

            //Create and add two HighscoreEntry-objects per PlayedGame
            for(PlayedGame playedGame : playedGameList) {
                highscoreList.add(new HighscoreEntry(playedGame.getTimeStamp(), playedGame.getUser1(), playedGame.getUserScore1()));
                highscoreList.add(new HighscoreEntry(playedGame.getTimeStamp(), playedGame.getUser2(), playedGame.getUserScore2()));
            }

            //Sort the highscoreList by score and then by date
            Collections.sort(highscoreList, new ScoreEntryScoreAndDateComparator());

            //Shrink the list to the maximum size of LENGTH_HIGHSCORE_LIST
            if(highscoreList.size() > GameConfig.LENGTH_HIGHSCORE_LIST) {
                highscoreList = highscoreList.subList(0, GameConfig.LENGTH_HIGHSCORE_LIST);
            }
        }

        //Return the List, null when no games have been played
        return ResponseEntity.ok(highscoreList);
    }

    /**
     * Returns a list of all played games of the calling user
     */
    @GetMapping( path = "/playedGames")
    public ResponseEntity<PlayedGameResult> getAllPlayedGames(){

        //Get the user from the context
        User user = userController.getUserFromSecurityContext(SecurityContextHolder.getContext());

        //Is the user present
        if(user != null) {

            //Get the userId
            int userId = user.getUserId();

            //List for all played games of the user
            List<UserScoreEntry> userGameList = new ArrayList<>();

            boolean wonGame;

            //Check all played games and create UserScoreEntry objects
            for(PlayedGame playedGameList : playedGameRepository.findAll()) {

                if(playedGameList.getUser1().getUserId() == userId) {

                    //The first player is the calling user
                    userGameList.add(new UserScoreEntry(playedGameList.getTimeStamp(), playedGameList.getUserScore1(), playedGameList.getUserScore2(), playedGameList.getUser2()));

                } else if (playedGameList.getUser2().getUserId() == userId) {

                    //The second player is the calling user
                    userGameList.add(new UserScoreEntry(playedGameList.getTimeStamp(), playedGameList.getUserScore2(), playedGameList.getUserScore1(), playedGameList.getUser1()));
                }
            }

            //Check if there are entries in the list
            if(userGameList.size() > 0) {

                //Sort the list by date
                Collections.sort(userGameList, new ScoreEntryDateComparator());

                //Shrink the list to the maximum size of LENGTH_HIGHSCORE_LIST
                if(userGameList.size() > GameConfig.LENGTH_USER_PLAYED_GAMES_LIST) {
                    userGameList = userGameList.subList(0, GameConfig.LENGTH_USER_PLAYED_GAMES_LIST);
                }

                //Create the PlayedGameResult
                PlayedGameResult result = new PlayedGameResult(userGameList);

                //Create a PlayedGameResult object and return it
                return ResponseEntity.ok(result);
            }
        }

        //No User or played games found, return an empty object
        return ResponseEntity.ok(new PlayedGameResult(null));
    }

    /**
     * The method stores a playedGame to the datebase and returns wheather one or both of the players achieved a new highscore
     * @param playedGame
     * @return
     */
    public Map<User, Boolean> submitPlayedGame(PlayedGame playedGame) {

        //Store the played game in the database
        playedGameRepository.save(playedGame);

        //Create the return map with default values
        Map<User, Boolean> isHighscore = new HashMap<>();
        isHighscore.put(playedGame.getUser1(), false);
        isHighscore.put(playedGame.getUser2(), false);

        //Get the new highscore list
        ResponseEntity<List<HighscoreEntry>> highscoreResponse = getHighScore();
        if(highscoreResponse.hasBody()) {
            List<HighscoreEntry> highscoreList = highscoreResponse.getBody();

            //Create HighscoreEntry object of both users
            HighscoreEntry highscoreEntryUser1 = new HighscoreEntry(playedGame.getTimeStamp(), playedGame.getUser1(), playedGame.getUserScore1());
            HighscoreEntry highscoreEntryUser2 = new HighscoreEntry(playedGame.getTimeStamp(), playedGame.getUser2(), playedGame.getUserScore2());

            //Check for highscores
            for(HighscoreEntry entry : highscoreList) {

                //Check user1 for highscore
                if(entry.equals(highscoreEntryUser1)) {
                    isHighscore.replace(playedGame.getUser1(), true);
                }

                //Check user2 for highscore
                if(entry.equals(highscoreEntryUser2)) {
                    isHighscore.replace(playedGame.getUser2(), true);
                }

                //Break when both users achieved a highscore
                if(isHighscore.get(playedGame.getUser1()) && isHighscore.get(playedGame.getUser2())) {
                    break;
                }
            }

            //Return the highscore map
            return isHighscore;
        } else {

            //Return false/false when there's no highscore available
            return isHighscore;
        }
    }
}
