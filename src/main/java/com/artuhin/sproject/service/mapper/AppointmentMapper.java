package com.artuhin.sproject.service.mapper;

import com.artuhin.sproject.model.dto.AppointmentSwitchDto;
import com.artuhin.sproject.model.dto.CreateAppointmentGetDto;
import com.artuhin.sproject.model.dto.MasterScheduleTableDto;
import com.artuhin.sproject.model.entity.AppointmentEntity;
import com.artuhin.sproject.model.AppointmentCreateGetModel;
import com.artuhin.sproject.model.dto.AppointmentGetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProcedureMapper procedureMapper;

    public AppointmentGetDto toAppointmentGetDto(AppointmentEntity entity) {
        return AppointmentGetDto.builder()
                .id(entity.getId())
                .master(userMapper.toCommonUserDto(entity.getMaster()))
                .client(userMapper.toCommonUserDto(entity.getClient()))
                .procedure(procedureMapper.toCommonProcedureDto(entity.getProcedure()))
                .startDate(entity.getStartTime())
                .endDate(entity.getEndTime())
                .status(entity.getAppointmentStatus())
                .build();
    }

    public List<AppointmentGetDto> toAppointmentGetDtoList(List<AppointmentEntity> entities) {
        return entities.stream().map(this::toAppointmentGetDto)
                .collect(Collectors.toList());
    }

    public CreateAppointmentGetDto toCreateAppGetDto(AppointmentCreateGetModel model) {
        return CreateAppointmentGetDto.builder()
                .masters(userMapper.toFullUserDtoList(model.getMasters()))
                .procedures(procedureMapper.toFullProcedureDtoList(model.getProcedures()))
                .build();
    }

    public MasterScheduleTableDto toMasterScheduleTableDto(AppointmentEntity entity){
        return MasterScheduleTableDto.builder().appointmentStatus(entity.getAppointmentStatus().toString().toLowerCase(Locale.ROOT)).startTime(entity.getStartTime().toLocalDateTime().toLocalTime().toString()).endTime(entity.getEndTime().toLocalDateTime().toLocalTime().toString()).build();
    }

    public List<MasterScheduleTableDto> toGetMasterScheduleTableDto (List<AppointmentEntity> entities){
        return entities.stream().map(this::toMasterScheduleTableDto).collect(Collectors.toList());
    }
}
