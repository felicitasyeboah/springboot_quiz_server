package de.semesterprojekt.quiz.database.repository;

import de.semesterprojekt.quiz.database.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The class implements the repository for the entity Question
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
