package de.semesterprojekt.quiz.repository;

import de.semesterprojekt.quiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
