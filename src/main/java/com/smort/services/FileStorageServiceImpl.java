package com.smort.services;

import com.smort.api.v1.mapper.FileMapper;
import com.smort.api.v1.model.FileInfoDTO;
import com.smort.api.v1.model.FileInfoListDTO;
import com.smort.domain.File;
import com.smort.error.FileStorageException;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private FileRepository fileRepository;

    public FileStorageServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    @Override
    public File storeFile(MultipartFile file) throws IOException {

        // normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (fileName.contains("..")) {
            throw new FileStorageException("Filename contains invalid path sequence " + fileName);
        }

        File dbFile = new File(fileName, file.getContentType(), file.getBytes());

        return fileRepository.save(dbFile);

    }

    @Override
    public File getFile(String fileId) {
        return fileRepository.findById(fileId).orElseThrow(()-> new ResourceNotFoundException("File not found with id " + fileId));
    }

    @Override
    public FileInfoDTO getFileInfoById(String fileId) {

        File file = fileRepository.findById(fileId).orElseThrow(()-> new ResourceNotFoundException("File not found with id " + fileId));

        return convertFileToDTO(file);

    }



    @Override
    public FileInfoListDTO getFileInfoList() {

        List<File> domainFiles = fileRepository.findAll();

        List<FileInfoDTO> files = domainFiles.stream().map(file -> {
            FileInfoDTO fileInfoDTO = convertFileToDTO(file);
            return fileInfoDTO;
        }).collect(Collectors.toList());

        return new FileInfoListDTO(files);
    }

    private FileInfoDTO convertFileToDTO(File file) {
        String fileId = file.getId();
        FileInfoDTO fileInfoDTO = FileMapper.INSTANCE.fileToFileInfoDTO(file);
        fileInfoDTO.setFileDownloadUri(UrlBuilder.getFileUri(fileId));
        fileInfoDTO.setSize((long)file.getData().length);

        DecimalFormat df = new DecimalFormat(".##");

        BigDecimal sizeKb = BigDecimal.valueOf((double)fileInfoDTO.getSize()/1024);
        sizeKb = sizeKb.setScale(2, RoundingMode.HALF_UP);

        String stringSize = sizeKb + "kb";

        fileInfoDTO.setSizeKb(stringSize);

        return fileInfoDTO;
    }


}
