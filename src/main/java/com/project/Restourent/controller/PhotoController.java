package com.project.Restourent.controller;

import com.project.Restourent.domain.dtos.response.ApiResponse;
import com.project.Restourent.domain.entities.Photo;
import com.project.Restourent.mappers.PhotoMapper;
import com.project.Restourent.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/photos")
public class PhotoController {
    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    @PostMapping
    public ResponseEntity<ApiResponse> uploadPhoto(@RequestParam("file")MultipartFile file) {
        Photo photo = photoService.uploadPhoto(file);
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK.value(),
                        "File Uploaded Successfully",
                        photoMapper.toDto(photo)
                ),
                HttpStatus.OK
        );
    }

    //public  void getPhoto
}
