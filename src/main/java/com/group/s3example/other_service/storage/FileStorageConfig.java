package com.group.s3example.other_service.storage;

import com.group.s3example.other_service.storage.s3.StorageS3;
import com.group.s3example.other_service.storage.s3.StorageS3Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FileStorageConfig {
    @Autowired
    private Environment env;

    @Bean
    public StorageResource storageResource() {
        String accessKey = env.getProperty("s3.access.key");
        String secretKey = env.getProperty("s3.secret.key");
        String bucket = env.getProperty("s3.bucket");
        String region = env.getProperty("s3.region");
        String endpoint = env.getProperty("s3.endpoint");
        return new StorageS3(new StorageS3Config(accessKey, secretKey, bucket, region, endpoint));
    }
}
