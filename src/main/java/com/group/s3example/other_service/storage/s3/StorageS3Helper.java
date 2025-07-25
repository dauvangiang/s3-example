package com.group.s3example.other_service.storage.s3;

import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

@Log4j2
public class StorageS3Helper {

    public List<String> listBuckets(S3Client s3Client) {
        return s3Client.listBuckets()
                .buckets()
                .stream()
                .map(Bucket::name)
                .toList();
    }

    public String writeFile(S3Client s3Client, BufferedInputStream inputStream, long contentLength, String path, String bucket) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(path)
                .checksumSHA256("AWS4-HMAC-SHA256")
                .build();
        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
        return path;
    }

    public InputStream readFile(S3Client s3Client, String path, String bucket) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(path)
                .build();
        try {
            return s3Client.getObject(getObjectRequest);
        } catch (Exception e) {
            log.error("readFile", e);
            return null;
        }
    }
}
