package com.smort.api.v1.mapper;

import com.smort.api.v1.model.FileInfoDTO;
import com.smort.domain.File;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    FileInfoDTO fileToFileInfoDTO(File file);

}
