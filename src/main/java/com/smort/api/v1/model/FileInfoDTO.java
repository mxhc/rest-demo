package com.smort.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDTO {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private Long size;
    private BigDecimal sizeKb;

    public FileInfoDTO(String fileName, String fileDownloadUri, String fileType, Long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }
}
