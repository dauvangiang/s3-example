package com.group.s3example.services.file_storage;

import com.group.s3example.other_service.storage.StorageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageService {
    @Autowired
    private StorageResource storageResource;

    public String createBucket(String bucketName) {
        return storageResource.createBucket(bucketName);
    }

    public String uploadFile(MultipartFile file) {
        try {
            return storageResource.writeFile(file.getInputStream(), file.getSize(), "s3_example/" + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String downloadFile(String bucketName, String key) {
        return null;
    }

}
