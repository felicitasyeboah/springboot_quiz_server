package de.semesterprojekt.quiz.fileservice.controller;

import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.database.controller.UserController;
import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.security.model.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;

/**
 * The class offers the rest mapping for the profile image upload and download
 */
@RestController
@CrossOrigin
public class ProfileImageController {

    @Autowired
    FileStorageService storageService;

    @Autowired
    UserController userController;

    @Autowired
    ProfileImageRenamer profileImageRenamer;

    /**
     * The method gets the file and sets it as profile image
     * @param file new profile image
     * @return Success or Fail message
     */
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {

        //Message for the console
        String message;

        //Get the fileName
        String fileName = file.getOriginalFilename();

        try {

            //Get the file extension
            String extension = "";
            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                extension = fileName.substring(i+1);
            }

            //Check if the file has an image extension
            if(Arrays.asList(GameConfig.ALLOWED_FILE_TYPE).contains(extension)) {

                //Get the user from the securityContext
                User user = userController.getUserFromSecurityContext(SecurityContextHolder.getContext());

                //Save the file
                storageService.save(file);

                //Rename the file uniquely and set it as profile image
                String newFileName = profileImageRenamer.rename(user, fileName, extension);

                //Create a message
                message = user.getUserName() + " uploaded the file '" + fileName + "' successfully.";

                //Print message
                System.out.println(message);

                //Print the new filename
                System.out.println("'" + fileName + "' successfully renamed to '" + newFileName + "'");

                //Return a HTTP response
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }
        } catch (Exception e) {

        }

        //Create a message
        message = "Could not upload the file. (Wrong file type)";

        //Print error message
        System.out.println(message);

        //Return a HTTP response
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }

    /**
     * Load the profile image by the userName
     * @param userName userName
     * @return profile image
     */
    @GetMapping("/profileImage/{userName}")
    @ResponseBody
    public ResponseEntity<Resource> getProfileImage(@PathVariable String userName) {

        Resource file;
        String fileName;
        final String defaultFileName = "default0.png";

        //Get the user by userName
        User user = userController.getUserFromUserName(userName);

        //Is the user available
        if (user != null) {

            //Get the profile image name
            fileName = user.getProfileImage();
        } else {

            //set the default file name
            fileName = defaultFileName;

            //Print error message
            System.out.println("User '" + userName + "' not found. Returning a default image.");
        }

        //try to load the image
        try {
            //Load the image
            file = storageService.load(fileName);
        } catch (Exception e) {

            //Load the default image
            file = storageService.load(defaultFileName);

            //Print error message
            System.out.println("Profile image of '" + userName + "' not found. Returning a default image.");
        }

        //Return the image
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}