package com.smort.services;

import com.smort.api.v1.model.FileInfoDTO;
import com.smort.api.v1.model.FileInfoListDTO;
import com.smort.domain.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    File storeFile(MultipartFile file) throws IOException;

    File getFile(String fileId);

    FileInfoDTO getFileInfoById(String fileId);

    FileInfoListDTO getFileInfoList();

}
