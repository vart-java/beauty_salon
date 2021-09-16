package com.artuhin.sproject.model.dto;

import com.artuhin.sproject.model.entity.RoleEntity;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CommonUserDto {
    private Long id;
    private String login;
    private Set<RoleEntity> roles;
    private double rating;
}
