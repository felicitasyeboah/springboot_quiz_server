package de.semesterprojekt.quiz.data.model;

import de.semesterprojekt.quiz.data.entity.User;
import lombok.Data;

/**
 * The class represents a simpler form of the user class
 */
@Data
public class SimpleUser {

    private String userName;
    private String profileImage;

    public SimpleUser(User user) {
        this.userName = user.getUserName();
        this.profileImage = user.getProfileImage();
    }
}
