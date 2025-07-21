package com.group.s3example.other_service.storage.s3;

import com.group.s3example.other_service.storage.StorageConfig;
import com.group.s3example.other_service.storage.StorageResource;
import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
//                .endpointOverride(URI.create(s3StorageConfig.getEndpoint()))
                .region(Region.of(s3StorageConfig.getRegion().toLowerCase()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(s3StorageConfig.getAccessKey(), s3StorageConfig.getSecretKey())))
//                .serviceConfiguration(S3Configuration.builder()
//                        .pathStyleAccessEnabled(true)
//                        .build())
                .build();

        log.info("S3 Storage connected!");
        return s3Client;
    }

    @Override
    public String createBucket(String bucketName) {
        S3Client s3Client = createS3();
        return s3StorageService.createBucket(s3Client, bucketName);
    }

    @Override
    public List<String> listBuckets() {
        S3Client s3Client = createS3();
        return s3StorageService.listBuckets(s3Client);
    }

    @Override
    public void deleteBucket(String bucket) {
        S3Client s3Client = createS3();
        s3StorageService.deleteBucket(s3Client, bucket);
    }

    @Override
    public List<String> listFiles(String bucket, String prefix) {
        S3Client s3Client = createS3();
        return s3StorageService.listFiles(s3Client, bucket, prefix);
    }

    @Override
    public String writeFile(InputStream inputStream, long contentLength, String path) {
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

    @Override
    public boolean deleteFile(String file) {
        log.info("s3 delete File: {}", file);
        S3Client amazonS3 = createS3();
        return s3StorageService.deleteFile(amazonS3, file, s3StorageConfig.getBucket());
    }

    @Override
    public void copyFile(String srcBucket, String srcFile, String destBucket, String destFile) {
        S3Client s3Client = createS3();
        s3StorageService.copyFile(s3Client, srcBucket, srcFile, destBucket, destFile);
    }

    @Override
    public void multipartUpload(String bucket, String path, InputStream input, long contentLength) {
        S3Client s3Client = createS3();
        try {
            s3StorageService.multipartUpload(s3Client, bucket, path, input, contentLength);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    @Override
    public Map<String, String> getMetadata(String bucket, String path) {
        S3Client s3Client = createS3();
        return s3StorageService.getMetadata(s3Client, bucket, path);
    }

    @Override
    public String getUrl(String file) {
        return null;
    }
}
