package de.semesterprojekt.quiz.repository;

import de.semesterprojekt.quiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The class implements the repository for the entity User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String username);
}
