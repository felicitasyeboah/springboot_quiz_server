package de.semesterprojekt.quiz.database.repository;

import de.semesterprojekt.quiz.database.entity.PlayedGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The class implements the repository for the entity PlayedGames
 */
@Repository
public interface PlayedGameRepository extends JpaRepository<PlayedGame, Integer> {
}
