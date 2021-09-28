package com.artuhin.sproject.service.mapper;

import com.artuhin.sproject.exception.ProcedureNotFoundException;
import com.artuhin.sproject.model.dto.CommonProcedureDto;
import com.artuhin.sproject.model.dto.CommonUserDto;
import com.artuhin.sproject.model.dto.FullUserDto;
import com.artuhin.sproject.model.dto.MasterDto;
import com.artuhin.sproject.model.pages.MastersTableModel;
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

    private Map<String, Consumer<List<MastersTableModel>>> actionMap;

    public UserMapper() {
        actionMap = new HashMap<>();
        actionMap.put("procedure", mastersRowDtos -> mastersRowDtos.sort(Comparator.comparing(MastersTableModel::getProcedureName)));
        actionMap.put("master", mastersRowDtos -> mastersRowDtos.sort(Comparator.comparing(MastersTableModel::getMasterName)));
        actionMap.put("rating", mastersRowDtos -> mastersRowDtos.sort(Comparator.comparingDouble(MastersTableModel::getRating).reversed()));
    }

    public CommonUserDto toCommonUserDto(UserEntity userEntity) {
        return CommonUserDto.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin().substring(0, userEntity.getLogin().indexOf('@')))
                .role(userEntity.getRole())
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
                .role(fullUserDto.getRole())
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
                .role(userEntity.getRole())
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

    public List<MastersTableModel> toMastersRowDtoList(List<UserEntity> entities) {
        if (ObjectUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        List<MastersTableModel> mastersTableModels = new ArrayList<>();
        for (UserEntity ue : entities) {
            for (ProcedureEntity pe : ue.getSkills()) {
                mastersTableModels.add(MastersTableModel.builder().masterName(ue.getLogin().substring(0, ue.getLogin().indexOf('@'))).procedureName(pe.getName()).rating(ue.getRating()).build());
            }
        }
        return mastersTableModels;
    }

    public List<MastersTableModel> sortMastersRowDtoList(String key, List<MastersTableModel> list) {
        actionMap.get(key).accept(list);
        return list;
    }

    public List<MastersTableModel> filterMasterRowDtoList(String filter, String by, List<MastersTableModel> list) {
        if (filter.equals("procedure")) {
            return list.stream().filter(m -> m.getProcedureName().equals(by)).collect(Collectors.toList());
        }
        if (filter.equals("master")) {
            return list.stream().filter(m -> m.getMasterName().equals(by)).collect(Collectors.toList());
        }
        if (filter.equals("rating")) {
            return list.stream().filter(m -> m.getRating() == Double.parseDouble(by)).collect(Collectors.toList());
        }
        throw new ProcedureNotFoundException("No procedures");
    }

    private MasterDto toMasterDto(UserEntity userEntity) {
        if (!userEntity.getRole().name().equals("MASTER")) {
            throw new ProcedureNotFoundException("Master not found");
        }
        return MasterDto.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin())
                .rating(userEntity.getRating())
                .skills(procedureMapper.toCommonProcedureDtoSet(userEntity.getSkills()))
                .build();
    }

    public Map<CommonProcedureDto, List<MasterDto>> getMastersRatingsAndBookModel(List<ProcedureEntity> procedureEntities, List<UserEntity> userEntities) {
        Map<CommonProcedureDto, List<MasterDto>> ratingMap = new HashMap<>();
        procedureEntities.forEach(p -> ratingMap.put(procedureMapper.toCommonProcedureDto(p), userEntities.stream().filter(m -> m.getSkills().stream().filter(s -> s.equals(p)).findAny().isPresent()).sorted(Comparator.comparingDouble(UserEntity::getRating).reversed()).map(this::toMasterDto).collect(Collectors.toList())));
        return ratingMap;
    }
}
