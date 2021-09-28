package com.artuhin.sproject.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class MasterDto {
    private Long id;
    private String login;
    private double rating;
    private Set<CommonProcedureDto> skills;
}
