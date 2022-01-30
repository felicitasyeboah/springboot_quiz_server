package de.semesterprojekt.quiz.fileservice.controller;

import de.semesterprojekt.quiz.database.controller.UserController;
import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.security.model.ResponseMessage;
import de.semesterprojekt.quiz.fileservice.storageservice.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@CrossOrigin
public class ProfileImageController {

    @Autowired
    FilesStorageService storageService;

    @Autowired
    UserController userController;

    @Autowired
    ProfileImageRenamer profileImageRenamer;

/*
    //TODO: DELETE
    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(ProfileImageController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }
*/
    /*
    /**
     * TODO: DELETE
     * Load the profile image by the fileName
     * @param filename filename
     * @return profile image
     */
    /*
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {

        System.out.println("/files/filename wird noch genutzt!");
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    */
    /**
     * The method gets the file and sets it as profile image
     * @param file new profile image
     * @return Success or Fail message
     */
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {

        try {

            //Get the user from the securityContext
            User user = userController.getUserFromSecurityContext(SecurityContextHolder.getContext());

            //Save the file
            storageService.save(file);

            //Rename the file uniquely and set it as profile image
            profileImageRenamer.rename(user, file.getOriginalFilename());

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "."));
        }
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
            System.out.println("Profile image not found. Returning a default image.");
        }

        //Return the image
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}