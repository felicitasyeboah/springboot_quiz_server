package de.semesterprojekt.quiz.fileservice.controller;

import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.database.repository.UserRepository;
import de.semesterprojekt.quiz.fileservice.model.FileInfo;
import de.semesterprojekt.quiz.security.model.ResponseMessage;
import de.semesterprojekt.quiz.fileservice.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@CrossOrigin
public class FilesController {

    @Autowired
    FilesStorageService storageService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRenamer fileRenamer;

    /**
     * The method gets the file and sets it as profile image
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {

        try {

            String username = "";

            //Get username from context
            Object principal = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();

            //Set the username
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal)
                        .getUsername();
            } else {
                username = principal
                        .toString();
            }

            //Get the User
            User user = (User) userRepository.findByUserName(username).get();

            //Save the file
            storageService.save(file);

            //Rename the file uniquely and set it as profile image
            fileRenamer.rename(user, file.getOriginalFilename());

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Uploaded the file successfully: " + file.getOriginalFilename()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("Could not upload the file: " + file.getOriginalFilename() + "!"));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}