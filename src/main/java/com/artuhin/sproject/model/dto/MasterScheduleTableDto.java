package com.artuhin.sproject.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MasterScheduleTableDto {
    private String appointmentStatus;
    private String startTime;
    private String endTime;
}
