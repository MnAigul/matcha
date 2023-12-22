package com.example.matcha.web;

import com.example.matcha.entity.User;
import com.example.matcha.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    @Autowired
    private UserService userService;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    public static final String UPLOAD_DIR = "/Users/macbook/Desktop/my_school21_projects/matcha/matcha/src/main/resources/static/uploads/";

    @Autowired
    private ServletContext servletContext;

    @PostMapping("/uploadFile/{id}")
    public ResponseEntity<String> handleFileUpload(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        try {
            String type = file.getContentType().split("/")[1];
            String fileName = id + "." + type;
            File destFile = new File(UPLOAD_DIR + fileName);
            file.transferTo(destFile);
            userService.updatePhoto(Long.valueOf(id), fileName);


            return ResponseEntity.ok("File uploaded successfully");
        } catch (Error | IOException e) {
            LOG.info(e.getMessage());
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/getPhoto/{id}")
    public ResponseEntity<Resource> getFotoProfile(@PathVariable String id) throws MalformedURLException {
        Long userId = Long.valueOf(id);
        User user = userService.getUserById(userId);
        String type = user.getPhoto().split("\\.")[1];
        String name = user.getPhoto().split("\\.")[0];
        Path filePath = Paths.get(UPLOAD_DIR).resolve(name + "." + type).normalize();
//        Resource resource = new FileSystemResource(filePath);
        Resource resource = new UrlResource(filePath.toUri());
        System.out.println(resource);
        System.out.println("filepath = " + filePath);
        System.out.println("resource.getFilename() =  " + resource.getFilename());
        if (resource.exists()) {
            return ResponseEntity.ok().body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//        if (resource.exists()) {
//            return ResponseEntity.ok().body(resource);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }


}
