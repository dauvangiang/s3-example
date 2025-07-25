package com.group.s3example.other_service.storage.s3;

import com.group.s3example.other_service.storage.StorageConfig;
import com.group.s3example.other_service.storage.StorageResource;
import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

@Log4j2
public class StorageS3 implements StorageResource {
    private final StorageS3Config s3StorageConfig;
    private final StorageS3Helper s3StorageService;

    public StorageS3(StorageConfig s3StorageConfig) {
        this.s3StorageConfig = (StorageS3Config) s3StorageConfig;
        this.s3StorageService = new StorageS3Helper();
    }

    private S3Client createS3() {
        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create(s3StorageConfig.getEndpoint()))
                .region(Region.of(s3StorageConfig.getRegion().toLowerCase()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(s3StorageConfig.getAccessKey(), s3StorageConfig.getSecretKey())))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();

        log.info("S3 Storage connected!");
        return s3Client;
    }

    @Override
    public List<String> listBuckets() {
        S3Client s3Client = createS3();
        return s3StorageService.listBuckets(s3Client);
    }

    @Override
    public String writeFile(BufferedInputStream inputStream, long contentLength, String path) {
        S3Client s3Client = createS3();
        return s3StorageService.writeFile(s3Client, inputStream, contentLength, path, this.s3StorageConfig.getBucket());
    }

    @Override
    public InputStream readFile(String path) {
        log.info("s3 read File: {}", path);
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return s3StorageService.readFile(createS3(), path, s3StorageConfig.getBucket());
    }
}
