package com.artuhin.sproject.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CreateAppointmentGetDto {
    private List<FullUserDto> masters;
    private List<FullProcedureDto> procedures;
}
