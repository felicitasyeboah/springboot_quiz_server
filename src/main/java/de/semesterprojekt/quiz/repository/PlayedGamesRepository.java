package de.semesterprojekt.quiz.repository;

import de.semesterprojekt.quiz.entity.PlayedGames;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayedGamesRepository extends JpaRepository<PlayedGames, Integer> {
}
