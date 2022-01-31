package de.semesterprojekt.quiz.fileservice.controller;

import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.database.repository.UserRepository;
import de.semesterprojekt.quiz.fileservice.storageservice.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.File;
import java.util.Random;
import java.util.UUID;

/**
 * The class offers a method to uniquely rename profile images
 */
@Controller
@CrossOrigin
public class ProfileImageRenamer {

    @Autowired
    FilesStorageService storageService;

    @Autowired
    UserRepository userRepository;

    private final String root = "./src/main/resources/images/";

    /**
     * The method renames the uploaded file to an unique file name and sets it as profile image of a user
     * @param user User, which profile image shall be changed
     * @param fileName New file name
     */
    public void rename(User user, String fileName) {

        if(user != null) {
            try {

                //delete the old image if it's no default picture
                if(!user.getProfileImage().substring(0,7).equals("default")) {
                    File oldFile = new File(root + user.getProfileImage());
                    oldFile.delete();
                }

                //Get the file
                File file = new File(root + fileName);

                //Get the file extension
                String extension = "";
                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i+1);
                }

                //Create an unique file name
                String uniqueFileName = UUID.randomUUID() + "." + extension.toLowerCase();

                //Rename file
                file.renameTo(new File(root + uniqueFileName));

                //Set the new profile image
                user.setProfileImage(uniqueFileName);

                //Print the username
                System.out.println("User '" + user.getUserName() + "' updated its profile image.");

            } catch (Exception e) {

                //Set a default picture
                Random rand = new Random();
                String defaultImage = "default" + rand.nextInt(14) + ".png";
                user.setProfileImage(defaultImage);

                //Print error message
                System.out.println("User '" + user.getUserName() + "' failed to update its profile image.");

            } finally {

                //Save the user
                userRepository.save(user);
            }
        } else {

            //Print error message when no user is available
            System.out.println("No user available.");
        }
    }
}