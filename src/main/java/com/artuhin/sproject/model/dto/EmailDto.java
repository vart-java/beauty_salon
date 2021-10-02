package com.artuhin.sproject.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EmailDto {
    private long appointmentId;
    private long masterId;
    private String masterName;
    private String procedureName;
    private String clientLogin;
    private LocalDate startDate;
}
