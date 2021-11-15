package de.semesterprojekt.quiz.repository;

import de.semesterprojekt.quiz.entity.HighscoreGlobal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HighscoreGlobalRepository extends JpaRepository<HighscoreGlobal, Integer> {
}
