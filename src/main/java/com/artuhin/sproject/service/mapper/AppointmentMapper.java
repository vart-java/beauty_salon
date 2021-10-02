package com.artuhin.sproject.service.mapper;

import com.artuhin.sproject.model.dto.BookMasterDailyScheduleTableDto;
import com.artuhin.sproject.model.dto.EmailDto;
import com.artuhin.sproject.model.dto.MasterScheduleTableDto;
import com.artuhin.sproject.model.entity.AppointmentEntity;
import com.artuhin.sproject.model.dto.AppointmentGetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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
                .startDate(entity.getStartTime().toLocalDateTime())
                .endDate(entity.getStartTime().toLocalDateTime().plusSeconds(entity.getProcedure().getDuration().getSeconds()))
                .status(entity.getAppointmentStatus())
                .build();
    }

    public EmailDto toEmailDto(AppointmentEntity entity) {
        return EmailDto.builder()
                .appointmentId(entity.getId())
                .startDate(entity.getStartTime().toLocalDateTime().toLocalDate())
                .masterName(entity.getMaster().getUsername())
                .masterId(entity.getMaster().getId())
                .clientLogin(entity.getClient().getLogin())
                .procedureName(entity.getProcedure().getName())
                .build();
    }

    public List<AppointmentGetDto> toAppointmentGetDtoList(List<AppointmentEntity> entities) {
        return entities.stream().map(this::toAppointmentGetDto)
                .collect(Collectors.toList());
    }

    public MasterScheduleTableDto toMasterScheduleTableDto(AppointmentEntity entity) {
        return MasterScheduleTableDto.builder().appointmentStatus(entity.getAppointmentStatus().toString()).startTime(entity.getStartTime().toLocalDateTime().toLocalTime().toString()).endTime(entity.getStartTime().toLocalDateTime().plusSeconds(entity.getProcedure().getDuration().getSeconds()).toLocalTime().toString()).build();
    }

    public List<MasterScheduleTableDto> toGetMasterScheduleTableDto(List<AppointmentEntity> entities) {
        return entities.stream().map(this::toMasterScheduleTableDto).collect(Collectors.toList());
    }

    private BookMasterDailyScheduleTableDto toBookMasterDailyScheduleTableDto(AppointmentEntity entity) {
        return BookMasterDailyScheduleTableDto.builder().startTime(entity.getStartTime().toLocalDateTime().toLocalTime().toString()).endTime(entity.getStartTime().toLocalDateTime().plusSeconds(entity.getProcedure().getDuration().getSeconds()).toLocalTime().toString()).status(entity.getAppointmentStatus().toString()).build();
    }

    public List<BookMasterDailyScheduleTableDto> toBookMasterDailyScheduleTableDtos(List<AppointmentEntity> entities) {
        return entities.stream().map(this::toBookMasterDailyScheduleTableDto).collect(Collectors.toList());
    }

    public List<EmailDto> toEmailDtoList(List<AppointmentEntity> list) {
        return list.stream().map(this::toEmailDto).collect(Collectors.toList());
    }
}
