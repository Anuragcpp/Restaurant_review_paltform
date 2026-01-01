package com.project.Restourent.controller;

import com.project.Restourent.domain.dtos.PhotoDto;
import com.project.Restourent.domain.entities.Photo;
import com.project.Restourent.mappers.PhotoMapper;
import com.project.Restourent.service.PhotoService;
import com.project.Restourent.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/photos")
public class PhotoController {
    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    @PostMapping
    public PhotoDto uploadPhoto(@RequestParam("file")MultipartFile file) {
        Photo photo = photoService.uploadPhoto(file);
        return photoMapper.toDto(photo);
    }
}
