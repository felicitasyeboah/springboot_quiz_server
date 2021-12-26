package de.semesterprojekt.quiz.database.repository;

import de.semesterprojekt.quiz.database.entity.HighscoreGlobal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The class implements the repository for the entity HighscoreGlobal
 */
@Repository
public interface HighscoreGlobalRepository extends JpaRepository<HighscoreGlobal, Integer> {
}
