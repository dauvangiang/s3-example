package com.group.s3example.controller;

import com.group.s3example.dto.BaseResponse;
import com.group.s3example.dto.UploadRes;
import com.group.s3example.services.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    @PostMapping("upload")
    public ResponseEntity<?> upload(
            @RequestHeader("APP-KEY") String appKey,
            @RequestParam("file") MultipartFile file,
            @RequestParam("document_group") String documentGroup,
            @RequestParam("document_id") String documentId
    ) {
        BaseResponse<UploadRes> res = fileStorageService.uploadFile(file, documentGroup, documentId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("download/{path}")
    public ResponseEntity<?> download(@PathVariable String path) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path + "\"")
                .body(new InputStreamResource(fileStorageService.downloadFile(path)));
    }

    @PostMapping("/webhook/status")
    public ResponseEntity<?> webhook(@RequestBody Object request) {
        log.info(request);
        return ResponseEntity.ok().build();
    }
}
