package com.example.tecnoscloud.documents.repo;

import com.example.tecnoscloud.documents.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {
}
