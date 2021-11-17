package de.semesterprojekt.quiz.repository;

import de.semesterprojekt.quiz.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
