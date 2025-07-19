package com.group.s3example.other_service.storage.s3;

import com.group.s3example.other_service.storage.StorageConfig;
import com.group.s3example.other_service.storage.StorageResource;
import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;

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
                .endpointOverride(URI.create(s3StorageConfig.getEndpoint()))
                .region(Region.of(s3StorageConfig.getRegion().toLowerCase()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(s3StorageConfig.getAccessKey(), s3StorageConfig.getSecretKey())))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();

        log.warn("S3 Storage connected!");
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

    @Override
    public String createBucket(String bucketName) {
        S3Client s3Client = createS3();

        try {
            ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                    .bucket("portal")
                    .maxKeys(10)
                    .build();

            s3Client.listObjectsV2(listReq).contents().forEach((S3Object obj) -> {
                System.out.println("- " + obj.key());
            });
        } catch (Exception e) {
            System.err.println("Không thể list object trong bucket 'portal': " + e.getMessage());
        }

//        ListBucketsResponse buckets = s3Client.listBuckets();
//        for (Bucket b : buckets.buckets()) {
//            System.out.println("🔎 Bucket: " + b.name());
//        }

//        CreateBucketRequest request = CreateBucketRequest.builder()
//                .bucket(bucketName)
//                .build();
//
//        s3Client.createBucket(request);
//        System.err.println("✅ Bucket created: " + bucketName);
        return "";
    }

    public void uploadTestFile() {
        S3Client s3Client = createS3();
        String bucketName = "portal"; // bucket mặc định từ file XML
        String objectKey = "test-folder/hello.txt";

        // Nội dung file
        String fileContent = "Xin chào từ Viettel Cloud!";

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType("text/plain")
                .build();

        s3Client.putObject(putRequest, RequestBody.fromString(fileContent));

        System.out.println("✅ Đã upload object thành công: " + objectKey);
    }
}
