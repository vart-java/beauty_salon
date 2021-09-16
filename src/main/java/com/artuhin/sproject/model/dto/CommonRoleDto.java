package com.artuhin.sproject.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonRoleDto {
    private Long id;
    private String name;

}
