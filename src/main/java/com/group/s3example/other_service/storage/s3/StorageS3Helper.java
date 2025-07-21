package com.group.s3example.other_service.storage.s3;

import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Log4j2
public class StorageS3Helper {
    public String createBucket(S3Client s3Client, String bucketName) {
        CreateBucketRequest createRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();
        try {
            s3Client.createBucket(createRequest);
            return bucketName;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public List<String> listBuckets(S3Client s3Client) {
        return s3Client.listBuckets()
                .buckets()
                .stream()
                .map(Bucket::name)
                .toList();
    }

    public void deleteBucket(S3Client s3Client, String bucket) {
        s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(bucket).build());
    }

    public List<String> listFiles(S3Client s3Client, String bucket, String prefix) {
        ListObjectsV2Response res = s3Client.listObjectsV2(
                ListObjectsV2Request.builder().bucket(bucket).prefix(prefix).build()
        );
        return res.contents().stream().map(S3Object::key).toList();
    }

    public String writeFile(S3Client s3Client, InputStream inputStream, long contentLength, String path, String bucket) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(path)
                .build();
        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
        return path;
    }

    public InputStream readFile(S3Client s3Client, String path, String bucket) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(path)
                .build();
        try (ResponseInputStream<GetObjectResponse> s3InputStream = s3Client.getObject(getObjectRequest)) {
            return s3InputStream;
        } catch (IOException e) {
            log.error("readFile", e);
            return null;
        }
    }

    public boolean deleteFile(S3Client s3Client, String path, String bucket) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(path)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    public void copyFile(S3Client s3Client, String sourceBucket, String srcFile, String destBucket, String destFile) {
        s3Client.copyObject(CopyObjectRequest.builder()
                .copySource(sourceBucket + "/" + srcFile)
                .destinationBucket(destBucket)
                .destinationKey(destFile)
                .build());
    }

    public void multipartUpload(S3Client s3Client, String bucket, String path, InputStream input, long contentLength) throws IOException {
        // Tạo multipart upload
        CreateMultipartUploadResponse createRes = s3Client.createMultipartUpload(
                CreateMultipartUploadRequest.builder().bucket(bucket).key(path).build());

        String uploadId = createRes.uploadId();
        int partSize = 5 * 1024 * 1024;
        List<CompletedPart> completedParts = new ArrayList<>();

        byte[] buffer = new byte[partSize];
        int partNumber = 1;
        int bytesRead;
        while ((bytesRead = input.read(buffer)) > 0) {
            UploadPartResponse uploadRes = s3Client.uploadPart(UploadPartRequest.builder()
                            .bucket(bucket)
                            .key(path)
                            .uploadId(uploadId)
                            .partNumber(partNumber)
                            .contentLength((long) bytesRead)
                            .build(),
                    RequestBody.fromBytes(Arrays.copyOf(buffer, bytesRead))
            );
            completedParts.add(CompletedPart.builder()
                    .partNumber(partNumber)
                    .eTag(uploadRes.eTag())
                    .build());
            partNumber++;
        }

        // Hoàn tất upload
        s3Client.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(path)
                .uploadId(uploadId)
                .multipartUpload(CompletedMultipartUpload.builder().parts(completedParts).build())
                .build());
    }

    public Map<String, String> getMetadata(S3Client s3Client, String bucket, String path) {
        HeadObjectResponse metadata = s3Client.headObject(
                HeadObjectRequest.builder().bucket(bucket).key(path).build());
        return metadata.metadata();
    }
}
