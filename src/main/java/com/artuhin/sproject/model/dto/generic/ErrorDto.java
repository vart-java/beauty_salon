package com.artuhin.sproject.model.dto.generic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorDto {
    private String message;
    private List<String> fields;
}
