package de.semesterprojekt.quiz.entity;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @Column(unique = true)
    private String userName;

    private String password;

    private String profileImage;

    private boolean isOnline;
}
