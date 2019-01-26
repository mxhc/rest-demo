package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDTO {

    @ApiModelProperty(value = "File Name", example = "picture.jpg", position = 0)
    private String fileName;

    @ApiModelProperty(value = "File Type", example = "image/jpeg", position = 1)
    private String fileType;

    @ApiModelProperty(value = "File Size ib bytes", example = "254687", position = 2)
    private Long size;

    @ApiModelProperty(value = "File Size ib kbytes", example = "220kb", position = 3)
    private String sizeKb;

    @ApiModelProperty(value = "File download URI", example = "/api/v1/files/1f8d95d0-0f3a-4524-98f3-0ece328f4bc5", position = 4)
    private String fileDownloadUri;

    @ApiModelProperty(value = "Date Uploaded", example = "2019-01-26T16:22:11.582Z", position = 5)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dateUploaded;

    public FileInfoDTO(String fileName, String fileDownloadUri, String fileType, Long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }
}
