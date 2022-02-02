package de.semesterprojekt.quiz.database.repository;

import de.semesterprojekt.quiz.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The Interface implements the repository for the entity User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> { // <welche Entitaet wird uebergeben, welchen Datentyp hat der primaerschluessel>

    Optional<User> findByUserName(String username);
}