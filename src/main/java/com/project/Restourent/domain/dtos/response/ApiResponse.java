package com.project.Restourent.domain.dtos.response;

public record ApiResponse(
        int  Status,
        String message,
        Object data
) {
}
