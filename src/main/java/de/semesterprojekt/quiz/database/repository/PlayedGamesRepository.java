package de.semesterprojekt.quiz.database.repository;

import de.semesterprojekt.quiz.database.entity.PlayedGames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The class implements the repository for the entity PlayedGames
 */
@Repository
public interface PlayedGamesRepository extends JpaRepository<PlayedGames, Integer> {
}