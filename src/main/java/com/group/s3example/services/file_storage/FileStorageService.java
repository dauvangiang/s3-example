package com.group.s3example.services.file_storage;

import com.group.s3example.other_service.storage.StorageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.List;

@Service
public class FileStorageService {
    @Autowired
    private StorageResource storageResource;

    public String uploadFile(MultipartFile file) {
        try {
            return storageResource.writeResource(file.getInputStream(), "s3_example/" + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String downloadFile(String bucketName, String key) {
        return null;
    }

    public String createBucket(String bucketName) {
        storageResource.createBucket(bucketName);
        return bucketName;
    }

    public void uploadTestFile() {
        storageResource.uploadTestFile();
    }

    public void helloS3() {
        S3Client s3 = S3Client.builder()
                .region(Region.US_EAST_1)
                .build();

        try {
            ListBucketsResponse response = s3.listBuckets();
            List<Bucket> bucketList = response.buckets();
            bucketList.forEach(bucket -> {
                System.out.println("Bucket Name: " + bucket.name());
            });

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

}
