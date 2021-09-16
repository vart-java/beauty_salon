package com.artuhin.sproject.service.mapper;

import com.artuhin.sproject.model.dto.CommonProcedureDto;
import com.artuhin.sproject.model.dto.CommonRoleDto;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.model.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    public CommonRoleDto toCommonRoleDto(RoleEntity role) {
        return CommonRoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public Set<CommonRoleDto> toCommonRoleDtoSet(Set<RoleEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptySet();
        }
        return entities.stream().map(this::toCommonRoleDto)
                .collect(Collectors.toSet());
    }

    public RoleEntity fromCommonRoleDto(CommonRoleDto dto) {
        return RoleEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    public Set<RoleEntity> fromCommonRoleDtoSet(Set<CommonRoleDto> dtos) {
        if (Objects.isNull(dtos)) {
            return Collections.emptySet();
        }
        return dtos.stream().map(this::fromCommonRoleDto)
                .collect(Collectors.toSet());
    }
}
