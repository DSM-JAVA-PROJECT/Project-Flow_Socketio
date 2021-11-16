package com.projectflow.projectflow.domain.file.controller;

import com.projectflow.projectflow.domain.file.payload.FileResponse;
import com.projectflow.projectflow.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class FileController {

    private final FileService fileService;

    @PostMapping("/file")
    public FileResponse uploadFile(@RequestPart MultipartFile file) throws IOException {
        return fileService.uploadFile(file);
    }
}
