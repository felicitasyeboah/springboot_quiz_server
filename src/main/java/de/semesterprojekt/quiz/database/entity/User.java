package de.semesterprojekt.quiz.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.semesterprojekt.quiz.model.SimpleUser;
import lombok.Data;

import javax.persistence.*;
import java.util.Observable;

/**
 * The class represents a user
 */
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int userId;

    @Column (unique = true)
    private String userName;

    @JsonIgnore
    private String password;

    private String profileImage;

    @JsonIgnore
    public SimpleUser getSimpleUser () {

        return new SimpleUser(this);
    }
}
