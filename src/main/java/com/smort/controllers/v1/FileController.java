package com.smort.controllers.v1;

import com.smort.api.v1.model.FileInfoDTO;
import com.smort.api.v1.model.FileInfoListDTO;
import com.smort.domain.File;
import com.smort.services.FileStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "Files Controller")
@RequestMapping(FileController.BASE_URL)
@RestController
public class FileController {

    public static final String BASE_URL = "/api/v1/files";

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @ApiOperation(value = "Upload file")
    @PostMapping("/uploadFile")
    public FileInfoDTO uploadFile(@RequestParam MultipartFile file) throws IOException {
        File dbFile = fileStorageService.storeFile(file);

        String fileDowlnoadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/").path(dbFile.getId()).toUriString();

        return new FileInfoDTO(dbFile.getFileName(), fileDowlnoadUri, file.getContentType(), file.getSize());

    }

    @ApiOperation(value = "Upload multiple files")
    @PostMapping("/uploadMultipleFiles")
    public List<FileInfoDTO> uploadMultipleFiles(@RequestParam MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> {
                    try {
                        return uploadFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }


    @ApiOperation(value = "Get file by Id")
    @GetMapping("/{fileId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {

        File dbFile = fileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename:\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));

    }

    @ApiOperation(value = "Get file info by Id")
    @GetMapping("/{fileId}/info")
    @ResponseStatus(HttpStatus.OK)
    public FileInfoDTO getFileInfo(@PathVariable String fileId) {
        return fileStorageService.getFileInfoById(fileId);
    }

    @ApiOperation(value = "Get list of files")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public FileInfoListDTO getFileInfoList() {
        return fileStorageService.getFileInfoList();
    }
}
