package com.artuhin.sproject.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MastersTableDto {
    private String masterName;
    private String procedureName;
    private double rating;
}
