package com.artuhin.sproject.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonProcedureDto {
    private Long id;
    private String name;
}
