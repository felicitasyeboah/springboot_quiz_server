package de.semesterprojekt.quiz.repository;

import de.semesterprojekt.quiz.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
}
