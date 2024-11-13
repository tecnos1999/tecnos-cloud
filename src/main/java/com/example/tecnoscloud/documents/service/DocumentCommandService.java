package com.example.tecnoscloud.documents.service;
import com.example.tecnoscloud.documents.model.Document;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentCommandService {
    String uploadDocument(MultipartFile file);
}

