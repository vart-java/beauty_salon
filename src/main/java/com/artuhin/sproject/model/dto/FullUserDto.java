package com.artuhin.sproject.model.dto;

import com.artuhin.sproject.model.Role;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
public class FullUserDto {

    @NotNull
    private Long id;
    private String login;
    private String password;
    private double rating;
    private int recallCount;
    private Role role;
    private Set<CommonProcedureDto> skills;
}
