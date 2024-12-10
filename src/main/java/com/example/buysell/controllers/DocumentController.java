package com.example.buysell.controllers;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.buysell.services.DocumentService;

import java.io.InputStream;

@RestController
@RequestMapping("/download")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/receipt/{deliveryId}")
    public ResponseEntity<InputStreamResource> downloadReceipt(@PathVariable Long deliveryId) {
        InputStream inputStream = documentService.generatePdfReceipt(deliveryId);

        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream must not be null");
        }

        InputStreamResource resource = new InputStreamResource(inputStream);
        String filename = "receipt" + deliveryId + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}