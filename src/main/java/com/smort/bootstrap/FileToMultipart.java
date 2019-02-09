package com.smort.bootstrap;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;

public class FileToMultipart {

    public MultipartFile convertToMultipart(String fileName) throws IOException {

//        File diskFile = ResourceUtils.getFile("classpath:" + fileName);

        File diskFile = new File("products/" + fileName);

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
