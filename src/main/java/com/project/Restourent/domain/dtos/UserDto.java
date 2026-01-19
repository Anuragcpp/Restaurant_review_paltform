package com.project.Restourent.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String id;

    @NotBlank(message = "User name should be present")
    private String userName;

    @NotBlank(message = "GivenName should be present")
    private String givenName;

    @NotBlank(message = "Family name should be present")
    private String familyName;
}
