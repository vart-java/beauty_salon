package com.artuhin.sproject.model.dto;

import com.artuhin.sproject.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonUserDto {
    private Long id;
    private String login;
    private Role role;
    private double rating;
}
