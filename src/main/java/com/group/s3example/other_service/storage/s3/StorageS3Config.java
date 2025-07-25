package com.group.s3example.other_service.storage.s3;

import com.group.s3example.other_service.storage.StorageConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorageS3Config implements StorageConfig {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String region;
    private String endpoint;
}
