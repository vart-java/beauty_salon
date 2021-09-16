package com.artuhin.sproject.service.mapper;

import com.artuhin.sproject.model.dto.CommonUserDto;
import com.artuhin.sproject.model.dto.FullUserDto;
import com.artuhin.sproject.model.dto.MastersRowDto;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.model.entity.UserEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private ProcedureMapper procedureMapper;
    @Autowired
    private RoleMapper roleMapper;

    private  Map<String, Consumer<List<MastersRowDto>>> actionMap;

    public UserMapper() {
        actionMap = new HashMap<>();
        actionMap.put("procedure", mastersRowDtos -> mastersRowDtos.sort(Comparator.comparing(MastersRowDto::getProcedureName)));
        actionMap.put("master", mastersRowDtos -> mastersRowDtos.sort(Comparator.comparing(MastersRowDto::getMasterName)));
        actionMap.put("rating", mastersRowDtos -> mastersRowDtos.sort(Comparator.comparingDouble(MastersRowDto::getRating).reversed()));
    }

    public CommonUserDto toCommonUserDto(UserEntity userEntity) {
        return CommonUserDto.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin())
                .roles(userEntity.getRoles())
                .rating(userEntity.getRating())
                .build();
    }

    public Set<CommonUserDto> toCommonUserDtoSet(Set<UserEntity> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptySet();
        }
        return entities.stream().map(this::toCommonUserDto)
                .collect(Collectors.toSet());
    }

    public UserEntity fromFullUserDto(FullUserDto fullUserDto) {
        return UserEntity.builder()
                .id(fullUserDto.getId())
                .login(fullUserDto.getLogin())
                .password(fullUserDto.getPassword())
                .roles(roleMapper.fromCommonRoleDtoSet(fullUserDto.getRoles()))
                .rating(fullUserDto.getRating())
                .recallCount(fullUserDto.getRecallCount())
                .skills(procedureMapper.fromCommonProcedureDtoSet(fullUserDto.getSkills()))
                .build();
    }

    public FullUserDto toFullUserDto(UserEntity userEntity) {
        return FullUserDto.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin())
                .password(userEntity.getPassword())
                .roles(roleMapper.toCommonRoleDtoSet(userEntity.getRoles()))
                .rating(userEntity.getRating())
                .recallCount(userEntity.getRecallCount())
                .skills(procedureMapper.toCommonProcedureDtoSet(userEntity.getSkills()))
                .build();
    }

    public List<FullUserDto> toFullUserDtoList(List<UserEntity> entities) {
        if (ObjectUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toFullUserDto)
                .collect(Collectors.toList());
    }

    public List<MastersRowDto> toMastersRowDtoList(List<UserEntity> entities) {
        if (ObjectUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        List<MastersRowDto> mastersRowDtos = new ArrayList<>();
        for (UserEntity ue : entities) {
            for (ProcedureEntity pe : ue.getSkills()) {
                mastersRowDtos.add(MastersRowDto.builder().masterName(ue.getLogin().substring(0,ue.getLogin().indexOf('@'))).procedureName(pe.getName()).rating(ue.getRating()).build());
            }
        }
        return mastersRowDtos;
    }

    public List<MastersRowDto> sortMastersRowDtoList(String key, List<MastersRowDto> list){
        actionMap.get(key).accept(list);
        return list;
    }
}
