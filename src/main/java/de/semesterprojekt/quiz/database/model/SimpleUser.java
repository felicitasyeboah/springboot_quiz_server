package de.semesterprojekt.quiz.database.model;

import de.semesterprojekt.quiz.database.entity.User;
import lombok.Data;

/** TODO: Delete profileImage
 * The class represents a simpler form of the user class
 */
@Data
public class SimpleUser {

    private String userName;
    //private String profileImage;

    public SimpleUser(User user) {
        this.userName = user.getUserName();
        //this.profileImage = user.getProfileImage();
    }
}