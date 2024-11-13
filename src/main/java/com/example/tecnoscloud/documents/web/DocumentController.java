package com.example.tecnoscloud.documents.web;

import com.example.tecnoscloud.documents.service.DocumentCommandService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/document/")
@AllArgsConstructor
public class DocumentController {
    private final DocumentCommandService documentCommandService;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = documentCommandService.uploadDocument(file);
            return new ResponseEntity<>(fileUrl, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Eroare la încărcarea documentului.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("path/to/uploaded/files").resolve(filename).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



}
