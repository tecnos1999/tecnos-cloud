package com.example.tecnoscloud.documents.web;

import com.example.tecnoscloud.documents.model.Document;
import com.example.tecnoscloud.documents.service.DocumentCommandService;
import com.example.tecnoscloud.documents.service.DocumentQueryService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/document/")
@AllArgsConstructor
public class DocumentController {
    private final DocumentCommandService documentCommandService;
    private final DocumentQueryService documentQueryService;


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
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) {
        try {
            Optional<Document> documentOptional = documentQueryService.getDocumentByName(filename);

            if (documentOptional.isPresent()) {
                Document document = documentOptional.get();

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(document.getFileType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                        .body(document.getFileData());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



}