package com.example.tecnoscloud.documents.service;


import com.example.tecnoscloud.documents.dto.DocumentResponseDTO;
import com.example.tecnoscloud.documents.model.Document;
import com.example.tecnoscloud.documents.repo.DocumentRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DocumentCommandServiceImpl implements DocumentCommandService {

    private final DocumentRepo documentRepository;

    public DocumentCommandServiceImpl(DocumentRepo documentRepository) {
        this.documentRepository = documentRepository;
    }


    @Override
    public List<DocumentResponseDTO> uploadDocuments(List<MultipartFile> files) {
        List<DocumentResponseDTO> fileResponses = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                byte[] fileData = file.getBytes();
                String fileUrl = "http://localhost:8082/server/api/v1/document/files/" + file.getOriginalFilename();
                String fileType = Objects.requireNonNull(file.getContentType());

                Document document = Document.builder()
                        .name(file.getOriginalFilename())
                        .fileType(fileType)
                        .fileUrl(fileUrl)
                        .fileData(fileData)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                documentRepository.save(document);

                fileResponses.add(new DocumentResponseDTO(fileUrl, fileType));

            } catch (Exception e) {
                throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
            }
        }

        return fileResponses;
    }




}

