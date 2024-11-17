package com.example.tecnoscloud.documents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDTO {
    private String url;
    private String type;
}
