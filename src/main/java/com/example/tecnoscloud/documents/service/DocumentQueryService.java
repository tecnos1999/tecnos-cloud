package com.example.tecnoscloud.documents.service;
import com.example.tecnoscloud.documents.model.Document;
import java.util.Optional;

public interface DocumentQueryService {
    Optional<Document> getDocumentByName(String name);
    Optional<Document> findByFileUrl(String fileUrl);

}
