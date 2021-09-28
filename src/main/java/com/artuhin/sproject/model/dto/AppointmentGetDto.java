package com.artuhin.sproject.model.dto;

import com.artuhin.sproject.model.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentGetDto {

    private Long id;
    private CommonUserDto client;
    private CommonUserDto master;
    private CommonProcedureDto procedure;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private AppointmentStatus status;
}
