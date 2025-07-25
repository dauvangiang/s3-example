package com.group.s3example.services.storage;

import com.group.s3example.other_service.storage.StorageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class FileStorageService {
    @Autowired
    private StorageResource storageResource;

    public List<String> getListBuckets() {
        return storageResource.listBuckets();
    }

    public String createBucket(String bucket) {
        return storageResource.createBucket(bucket);
    }

    public void deleteBucket(String bucket) {
        storageResource.deleteBucket(bucket);
    }

    public List<String> getListFiles(String bucket, String prefix) {
        return storageResource.listFiles(bucket, prefix);
    }

    public String uploadFile(MultipartFile file) {
        try {
            return storageResource.writeFile(file.getInputStream(), file.getSize(), "s3_example/" + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String downloadFile(String bucket, String key) {
        return null;
    }

    public boolean deleteFile(String bucket, String path) {
        return storageResource.deleteFile(path);
    }

    public void copyFile(String srcBucket, String srcFile, String destBucket, String destFile) {
        storageResource.copyFile(srcBucket, srcFile, destBucket, destFile);
    }

    public void multipartUpload(MultipartFile file, String bucket, String path) {
        try {
            storageResource.multipartUpload(bucket, path, file.getInputStream(), file.getSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getMetadata(String bucket, String path) {
        return storageResource.getMetadata(bucket, path);
    }
}
