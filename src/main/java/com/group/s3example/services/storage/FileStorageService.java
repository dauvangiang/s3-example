package com.group.s3example.services.storage;

import com.group.s3example.dto.BaseResponse;
import com.group.s3example.dto.UploadRes;
import com.group.s3example.other_service.storage.StorageResource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileStorageService {
    @Autowired
    private StorageResource storageResource;

    public List<String> getListBuckets() {
        return storageResource.listBuckets();
    }

    public BaseResponse<UploadRes> uploadFile(MultipartFile file, String docGroup, String docId) {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream());
            String extend = FilenameUtils.getExtension(file.getOriginalFilename());
            String path = String.format("%s/%s/%s.%s", docGroup, docId, System.currentTimeMillis(), extend);
            storageResource.writeFile(inputStream, file.getSize(), path);
            return new BaseResponse<>(
                    new UploadRes(docGroup, docId, "/" + path)
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream downloadFile(String path) {
        InputStream inputStream = storageResource.readFile(path);
        return inputStream;
    }
}
