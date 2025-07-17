package com.group.s3example.other_service.storage;

import java.io.InputStream;

public interface StorageResource {
    InputStream readResource(String path);

    String writeResource(InputStream inputStream, String path);

    boolean deleteFile(String file);

    String getUrl(String file);
}
