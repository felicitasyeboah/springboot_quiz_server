package de.semesterprojekt.quiz.data.repository;

import de.semesterprojekt.quiz.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The class implements the repository for the entity Category
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
