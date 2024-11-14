package com.example.tecnoscloud.documents.service;


import com.example.tecnoscloud.documents.model.Document;
import com.example.tecnoscloud.documents.repo.DocumentRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class DocumentCommandServiceImpl implements DocumentCommandService {

    private final DocumentRepo documentRepository;

    public DocumentCommandServiceImpl(DocumentRepo documentRepository) {
        this.documentRepository = documentRepository;
    }


    @Override
    public String uploadDocument(MultipartFile file) {
        try {
            byte[] fileData = file.getBytes();

            String fileUrl = "http://localhost:8082/server/api/v1/document/files/" + file.getOriginalFilename();

            Document document = Document.builder()
                    .name(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileUrl(fileUrl)
                    .fileData(fileData)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            documentRepository.save(document);

            return fileUrl;

        } catch (Exception e) {
            throw new RuntimeException("Eroare la încărcarea documentului: " + e.getMessage());
        }
    }


}

