package com.example.tecnoscloud.documents.service;
import com.example.tecnoscloud.documents.dto.DocumentResponseDTO;
import com.example.tecnoscloud.documents.model.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentCommandService {
    List<DocumentResponseDTO> uploadDocuments(List<MultipartFile> files);
}

