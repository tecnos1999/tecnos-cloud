package com.example.tecnoscloud.documents.service;

import com.example.tecnoscloud.documents.dto.DocumentResponseDTO;
import com.example.tecnoscloud.documents.model.Document;
import com.example.tecnoscloud.documents.repo.DocumentRepo;
import com.example.tecnoscloud.exceptions.exception.AlreadyExistsException;
import com.example.tecnoscloud.exceptions.exception.AppException;
import com.example.tecnoscloud.exceptions.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DocumentCommandServiceImpl implements DocumentCommandService {

    private final DocumentRepo documentRepository;
    private final String fileBaseUrl;

    public DocumentCommandServiceImpl(DocumentRepo documentRepository,
                                      @Value("${file.baseUrl}") String fileBaseUrl) {
        this.documentRepository = documentRepository;
        this.fileBaseUrl = fileBaseUrl;
    }

    @Override
    public List<DocumentResponseDTO> uploadDocuments(List<MultipartFile> files) {
        List<DocumentResponseDTO> fileResponses = new ArrayList<>();
        for (MultipartFile file : files) {
            uploadSingleFile(file).ifPresent(fileResponses::add);
        }
        return fileResponses;
    }

    @Override
    public Optional<DocumentResponseDTO> uploadDocument(MultipartFile file) {
        return uploadSingleFile(file);
    }

    private Optional<DocumentResponseDTO> uploadSingleFile(MultipartFile file) {
        try {
            byte[] fileData = file.getBytes();
            String originalFileName = file.getOriginalFilename();
            validateFileName(originalFileName);

            String encodedFileName = encodeFileName(originalFileName);

            if (documentRepository.findByFileUrl(generateFileUrl(encodedFileName)).isPresent()) {
                throw new AlreadyExistsException("A document with the same URL already exists.");
            }

            String fileUrl = generateFileUrl(encodedFileName);

            Document document = buildDocument(encodedFileName, fileUrl, file.getContentType(), fileData);
            documentRepository.save(document);

            return Optional.of(new DocumentResponseDTO(fileUrl, document.getFileType()));
        } catch (AlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Error uploading file: " + file.getOriginalFilename());
        }
    }


    private void validateFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
    }

    private String encodeFileName(String fileName) {
        try {
            return URLEncoder.encode(fileName.replaceAll("\\s+", "_"), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding file name: " + fileName, e);
        }
    }

    private String generateFileUrl(String encodedFileName) {
        return UriComponentsBuilder.fromHttpUrl(fileBaseUrl)
                .path("/cloud/api/v1/document/files/")
                .pathSegment(encodedFileName)
                .toUriString();
    }

    private Document buildDocument( String encodedFileName, String fileUrl, String contentType, byte[] fileData) {
        return Document.builder()
                .name(encodedFileName)
                .fileType(Objects.requireNonNull(contentType, "File type cannot be null"))
                .fileUrl(fileUrl)
                .fileData(fileData)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public void deleteDocument(String fileUrl) {
        Optional<Document> documentOpt = documentRepository.findByFileUrl(fileUrl);
        if (documentOpt.isEmpty()) {
            throw new NotFoundException("Document not found with URL: " + fileUrl);
        }
        documentRepository.delete(documentOpt.get());
    }

    @Override
    public void deleteDocuments(List<String> fileUrls) {
        List<String> notFoundFiles = new ArrayList<>();

        for (String fileUrl : fileUrls) {
            try {
                deleteDocument(fileUrl);
            } catch (NotFoundException e) {
                notFoundFiles.add(fileUrl);
            }
        }

        if (!notFoundFiles.isEmpty()) {
            throw new NotFoundException("Some documents were not found: " + String.join(", ", notFoundFiles));
        }
    }

}
