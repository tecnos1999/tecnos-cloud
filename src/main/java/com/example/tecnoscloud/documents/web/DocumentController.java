package com.example.tecnoscloud.documents.web;

import com.example.tecnoscloud.documents.dto.DocumentResponseDTO;
import com.example.tecnoscloud.documents.model.Document;
import com.example.tecnoscloud.documents.service.DocumentCommandService;
import com.example.tecnoscloud.documents.service.DocumentQueryService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/cloud/api/v1/document/")
@AllArgsConstructor
public class DocumentController {
    private final DocumentCommandService documentCommandService;
    private final DocumentQueryService documentQueryService;


    @PostMapping("/upload")
    public ResponseEntity<List<DocumentResponseDTO>> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) {
        List<DocumentResponseDTO> fileResponses = documentCommandService.uploadDocuments(files);
        return ResponseEntity.ok(fileResponses);
    }

    @PostMapping("/uploadSingle")
    public ResponseEntity<DocumentResponseDTO> uploadSingleFile(@RequestParam("file") MultipartFile file) {
        Optional<DocumentResponseDTO> fileResponse = documentCommandService.uploadDocument(file);
        return fileResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) {
        try {
            Optional<Document> documentOptional = documentQueryService.getDocumentByName(filename);

            if (documentOptional.isPresent()) {
                Document document = documentOptional.get();

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(document.getFileType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getName() + "\"")
                        .body(document.getFileData());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/files")
    public ResponseEntity<String> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        documentCommandService.deleteDocument(fileUrl);
        return ResponseEntity.ok("File deleted successfully");
    }

    @DeleteMapping("/files/batch")
    public ResponseEntity<String> deleteFiles(@RequestBody List<String> fileUrls) {
        documentCommandService.deleteDocuments(fileUrls);
        return ResponseEntity.ok("Files deleted successfully");
    }






}
