package com.project.Restourent.mapper;

import com.project.Restourent.domain.dtos.ReviewCreateUpdateRequestDto;
import com.project.Restourent.domain.dtos.ReviewDto;
import com.project.Restourent.domain.entities.Review;
import com.project.Restourent.domain.entities.ReviewCreateUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
    ReviewCreateUpdateRequest toReviewCreateUpdateRequest(ReviewCreateUpdateRequestDto reviewCreateUpdateRequestDto);
    ReviewDto toDto (Review review);
}
