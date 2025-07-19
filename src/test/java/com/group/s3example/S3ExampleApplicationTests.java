package com.group.s3example;

import com.group.s3example.services.file_storage.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class S3ExampleApplicationTests {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    void testCreateBucket() {
        String bucketName = "first-bucket";
        fileStorageService.createBucket(bucketName);
    }

    @Test
    void contextLoads() {
        fileStorageService.uploadTestFile();
    }

    @Test
    void test() {
        fileStorageService.helloS3();
    }

}
