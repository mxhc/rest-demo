package com.smort.bootstrap;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;

public class FileToMultipart {

    public MultipartFile convertToMultipart(String fileName) throws IOException {

        ClassPathResource classPathResource = new ClassPathResource("products/" + fileName);

        InputStream inputStream = classPathResource.getInputStream();
        File diskFile = File.createTempFile(fileName, ".jpg");
        try {
            FileUtils.copyInputStreamToFile(inputStream, diskFile);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        FileItem fileItem = new DiskFileItem("file", Files.probeContentType(diskFile.toPath()), false, diskFile.getName(), (int) diskFile.length(), diskFile.getParentFile());

        InputStream in = new FileInputStream(diskFile);
        OutputStream out = fileItem.getOutputStream();

        try {
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }

        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

        return multipartFile;

    }

}
