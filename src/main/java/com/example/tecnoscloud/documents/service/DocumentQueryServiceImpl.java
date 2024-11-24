package com.example.tecnoscloud.documents.service;

import com.example.tecnoscloud.documents.model.Document;
import com.example.tecnoscloud.documents.repo.DocumentRepo;
import com.example.tecnoscloud.exceptions.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DocumentQueryServiceImpl implements DocumentQueryService {

    private final DocumentRepo documentRepository;

    @Override
    public Optional<Document> getDocumentByName(String name) {
        Optional<Document> documentOpt = documentRepository.findByName(name);
        if (documentOpt.isEmpty()) {
            throw new NotFoundException("Document not found with name: " + name);
        }
        return documentOpt;
    }


    @Override
    public Optional<Document> findByFileUrl(String fileUrl) {
        Optional<Document> documentOpt = documentRepository.findByFileUrl(fileUrl);
        if (documentOpt.isEmpty()) {
            throw new NotFoundException("Document not found with URL: " + fileUrl);
        }
        return documentOpt;
    }

}
