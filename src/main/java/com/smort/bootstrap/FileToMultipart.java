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

//        File diskFile = ResourceUtils.getFile("classpath:" + fileName);


//        File diskFile = new File("src/main/resources/products/" + fileName);

        ClassPathResource classPathResource = new ClassPathResource("products/" + fileName);

        InputStream inputStream = classPathResource.getInputStream();
        File diskFile = File.createTempFile(fileName, ".jpg");
        try {
            FileUtils.copyInputStreamToFile(inputStream, diskFile);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        FileItem fileItem = new DiskFileItem("file", Files.probeContentType(diskFile.toPath()), false, diskFile.getName(), (int) diskFile.length(), diskFile.getParentFile());

        InputStream input = new FileInputStream(diskFile);
        OutputStream os = fileItem.getOutputStream();
        IOUtils.copy(input, os);

        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

        input.close();
        os.close();

        return multipartFile;

    }

}
