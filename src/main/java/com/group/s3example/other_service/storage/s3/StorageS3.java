package com.group.s3example.other_service.storage.s3;

import com.group.s3example.other_service.storage.StorageConfig;
import com.group.s3example.other_service.storage.StorageResource;
import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.io.InputStream;
import java.net.URI;

@Log4j2
public class StorageS3 implements StorageResource {

    private final StorageS3Config s3StorageConfig;

    private final StorageS3Helper s3StorageService;

    public StorageS3(StorageConfig s3StorageConfig) {
        log.info("configure: {}", s3StorageConfig);
        this.s3StorageConfig = (StorageS3Config) s3StorageConfig;
        this.s3StorageService = new StorageS3Helper();
    }

    private S3Client createS3() {
        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create("https://vcos.cloudstorage.com.vn"))
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
    public InputStream readResource(String path) {
        log.info("s3 read File: {}", path);
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return s3StorageService.readFile(createS3(), path, s3StorageConfig.getBucket());
    }

    @Override
    public String writeResource(InputStream inputStream, String path) {
        S3Client s3Client = createS3();


//        ListBucketsResponse list = s3Client.listBuckets();
//        for (Bucket b : list.buckets()) {
//            System.err.println("Bucket: " + b.name());
//        }

        return s3StorageService.uploadFile(s3Client, inputStream, path, this.s3StorageConfig.getBucket());
    }

    @Override
    public boolean deleteFile(String file) {
        log.info("s3 delete File: {}", file);
        S3Client amazonS3 = createS3();
        return s3StorageService.deleteFile(amazonS3, file, s3StorageConfig.getBucket());
    }

    @Override
    public String getUrl(String file) {
        return null;
    }
}
