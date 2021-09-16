package com.artuhin.sproject.model.dto;

import com.artuhin.sproject.model.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class AppointmentGetDto {

    private Long id;
    private CommonUserDto client;
    private CommonUserDto master;
    private CommonProcedureDto procedure;
    private Timestamp startDate;
    private Timestamp endDate;
    private AppointmentStatus status;
}
