package com.artuhin.sproject.model.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class CreateAppointmentPostDto {
    private String procedureName;
    private Long masterId;
    private Long clientId;
    private Timestamp startDate;
}
