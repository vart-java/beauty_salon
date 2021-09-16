package com.artuhin.sproject.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class FullProcedureDto {

    private Long id;
    private String name;
    private long duration;
    private Set<CommonUserDto> masters;
}
