package com.group.s3example.other_service.storage;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface StorageResource {
    String createBucket(String bucketName);

    List<String> listBuckets();

    void deleteBucket(String bucket);

    List<String> listFiles(String bucket, String prefix);

    String writeFile(InputStream inputStream, long contentLength, String path);

    InputStream readFile(String path);

    boolean deleteFile(String file);

    void copyFile(String srcBucket, String srcFile, String destBucket, String destFile);

    void multipartUpload(String bucket, String path, InputStream input, long contentLength);

    Map<String, String> getMetadata(String bucket, String path);

    String getUrl(String file);
}
