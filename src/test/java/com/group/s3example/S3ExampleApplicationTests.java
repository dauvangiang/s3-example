package com.group.s3example;

import com.group.s3example.services.storage.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class S3ExampleApplicationTests {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    void testCreateBucket() {
//        String bucketName = "bucket-portal441";
//        fileStorageService.createBucket(bucketName);
        List<String> lst = fileStorageService.getListBuckets();
        for (String s : lst) {
            System.err.println(s);
        }
    }

}
