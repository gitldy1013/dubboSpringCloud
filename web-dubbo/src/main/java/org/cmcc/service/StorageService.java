package org.cmcc.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String storeFile(MultipartFile file);

    public String storeFile(MultipartFile file,String timeStamp);

    Resource loadFileAsResource(String fileName);
}
