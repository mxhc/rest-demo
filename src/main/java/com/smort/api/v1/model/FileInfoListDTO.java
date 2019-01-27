package com.smort.api.v1.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FileInfoListDTO {

    private List<FileInfoDTO> files;

    public FileInfoListDTO(List<FileInfoDTO> files) {
        this.files = files;
    }
}
