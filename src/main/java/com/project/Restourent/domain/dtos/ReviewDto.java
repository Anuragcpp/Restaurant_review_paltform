package com.project.Restourent.domain.dtos;

import com.project.Restourent.domain.entities.Photo;
import com.project.Restourent.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {

    private String id;

    private String content;

    private Integer rating;

    private LocalDateTime dataPosted;

    private LocalDateTime lastEdited;

    private List<PhotoDto> phots = new ArrayList<>();

    private UserDto writtenBy;
}
