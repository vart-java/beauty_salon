package com.artuhin.sproject.service.mapper;

import com.artuhin.sproject.model.dto.CommonProcedureDto;
import com.artuhin.sproject.model.dto.FullProcedureDto;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProcedureMapper {

    @Autowired
    private UserMapper userMapper;

    public CommonProcedureDto toCommonProcedureDto(ProcedureEntity procedure) {
        return CommonProcedureDto.builder()
                .id(procedure.getId())
                .name(procedure.getName())
                .build();
    }

    public ProcedureEntity fromCommonProcedureDto(CommonProcedureDto dto) {
        return ProcedureEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }

    public FullProcedureDto toFullProcedureDto(ProcedureEntity procedure) {
        return FullProcedureDto.builder()
                .id(procedure.getId())
                .name(procedure.getName())
                .duration(procedure.getDuration().toMinutes())
                .masters(userMapper.toCommonUserDtoSet(procedure.getMasters()))
                .build();
    }

    public Set<ProcedureEntity> fromCommonProcedureDtoSet(Set<CommonProcedureDto> dtos) {
        if (Objects.isNull(dtos)) {
            return Collections.emptySet();
        }
        return dtos.stream().map(this::fromCommonProcedureDto)
                .collect(Collectors.toSet());
    }

    public Set<CommonProcedureDto> toCommonProcedureDtoSet(Set<ProcedureEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptySet();
        }
        return entities.stream().map(this::toCommonProcedureDto)
                .collect(Collectors.toSet());
    }


    public List<FullProcedureDto> toFullProcedureDtoList(List<ProcedureEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toFullProcedureDto)
                .collect(Collectors.toList());
    }
}
