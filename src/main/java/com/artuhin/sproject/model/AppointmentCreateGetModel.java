package com.artuhin.sproject.model;

import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.model.entity.UserEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AppointmentCreateGetModel {

    private List<UserEntity> masters;
    private List<ProcedureEntity> procedures;
}
