package com.example.tecnoscloud.documents.service;
import com.example.tecnoscloud.documents.dto.DocumentResponseDTO;
import com.example.tecnoscloud.documents.model.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface DocumentCommandService {
    List<DocumentResponseDTO> uploadDocuments(List<MultipartFile> files);

    Optional<DocumentResponseDTO> uploadDocument(MultipartFile file);

    void deleteDocument(String fileUrl);

    void deleteDocuments(List<String> fileUrls);
}

