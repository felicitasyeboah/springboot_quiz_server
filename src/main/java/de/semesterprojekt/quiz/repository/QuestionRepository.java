package de.semesterprojekt.quiz.repository;

import de.semesterprojekt.quiz.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The class implements the repository for the entity Question
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
