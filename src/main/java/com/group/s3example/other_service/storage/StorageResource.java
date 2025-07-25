package com.group.s3example.other_service.storage;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

public interface StorageResource {
    List<String> listBuckets();

    String writeFile(BufferedInputStream inputStream, long contentLength, String path);

    InputStream readFile(String path);
}
