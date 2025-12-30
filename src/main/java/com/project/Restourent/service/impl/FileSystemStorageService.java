package com.project.Restourent.service.impl;

import com.project.Restourent.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Slf4j
public class FileSystemStorageService implements StorageService {
    @Override
    public String store(MultipartFile file, String fileName) {
        return "";
    }

    @Override
    public Optional<Resource> loadAsResource(String id) {
        return Optional.empty();
    }
}
