package com.example.tecnoscloud.documents.service;

import com.example.tecnoscloud.documents.model.Document;
import com.example.tecnoscloud.documents.repo.DocumentRepo;
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
            throw new RuntimeException("Document not found");
        }
        return documentOpt;
    }
}
