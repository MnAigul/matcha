package com.example.matcha.web;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileDownloadController {
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

//    @GetMapping("/download/{fileName:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
//        try {
//            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
//            System.out.println("Filepath = " + filePath);
//            Resource resource = new UrlResource(filePath.toUri());
//            if (resource.exists()) {
//                return ResponseEntity.ok().body(resource);
//            } else {
//                System.out.println("resoutce does not exists");
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return ResponseEntity.notFound().build();
//        }
//    }
}