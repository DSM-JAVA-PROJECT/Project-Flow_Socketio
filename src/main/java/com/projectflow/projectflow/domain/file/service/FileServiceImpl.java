package com.projectflow.projectflow.domain.file.service;

import com.projectflow.projectflow.domain.file.payload.FileResponse;
import com.projectflow.projectflow.global.s3.S3Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final S3Utils s3Utils;

    @Override
    public FileResponse uploadFile(MultipartFile file) throws IOException {
        String imageUrl = s3Utils.upload(file, "images/");
        return new FileResponse(imageUrl);
    }
}
