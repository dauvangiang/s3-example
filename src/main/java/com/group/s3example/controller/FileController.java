package com.group.s3example.controller;

import com.group.s3example.services.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    @PostMapping("upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileStorageService.uploadFile(file));
    }

    @GetMapping("download")
    public ResponseEntity<?> download() {
        return ResponseEntity.ok(fileStorageService.downloadFile("", ""));
    }
}
