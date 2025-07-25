package com.group.s3example.services.storage;

import com.group.s3example.other_service.storage.StorageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileStorageService {
    @Autowired
    private StorageResource storageResource;

    public List<String> getListBuckets() {
        return storageResource.listBuckets();
    }

    public String uploadFile(MultipartFile file) {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream());
            return storageResource.writeFile(inputStream, file.getSize(), System.currentTimeMillis() + "_" + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream downloadFile(String path) {
        InputStream inputStream = storageResource.readFile(path);
        return inputStream;
    }
}
