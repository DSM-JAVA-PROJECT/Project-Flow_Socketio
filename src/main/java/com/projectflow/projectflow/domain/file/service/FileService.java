package com.projectflow.projectflow.domain.file.service;

import com.projectflow.projectflow.domain.file.payload.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    FileResponse uploadFile(MultipartFile file) throws IOException;
}
