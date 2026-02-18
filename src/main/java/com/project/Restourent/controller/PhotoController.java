package com.project.Restourent.controller;

import com.project.Restourent.domain.dtos.response.ApiResponse;
import com.project.Restourent.domain.entities.Photo;
import com.project.Restourent.mapper.PhotoMapper;
import com.project.Restourent.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
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

    @GetMapping(path = "/{id:.+}")
    public  ResponseEntity<Resource> getPhoto (
            @PathVariable String id
    ){
        return photoService.getPhotoAsResource(id).map(
                photo ->
                        ResponseEntity.ok()
                                .contentType(
                                        MediaTypeFactory.getMediaType(photo)
                                                .orElse(MediaType.APPLICATION_OCTET_STREAM)
                                )
                                .header(HttpHeaders.CONTENT_DISPOSITION,"inline")
                                .body(photo)
        ).orElse(ResponseEntity.notFound().build());
    }
}
