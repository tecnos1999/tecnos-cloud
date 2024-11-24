package com.example.tecnoscloud.documents.repo;

import com.example.tecnoscloud.documents.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {

    Optional<Document> findByName(String name);

    Optional<Document> findByFileUrl(String fileUrl);
}
