package de.semesterprojekt.quiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * The class represents a user
 */
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column (unique = true)
    private String userName;

    @JsonIgnore
    private String password;

    private String profileImage;

    private boolean isReady;
}
