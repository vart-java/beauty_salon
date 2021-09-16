package com.artuhin.sproject.service;

import com.artuhin.sproject.model.AppointmentStatus;
import com.artuhin.sproject.model.dto.AppointmentSwitchDto;
import com.artuhin.sproject.model.dto.CreateAppointmentPostDto;
import com.artuhin.sproject.model.entity.AppointmentEntity;
import com.artuhin.sproject.model.AppointmentCreateGetModel;

import java.util.List;

public interface AppointmentService {
    /**
     * Create appointment from dto.
     * @param dto
     * @return
     */
    AppointmentEntity createAppointment(CreateAppointmentPostDto dto);

    /**
     * Get appointments by client.
     * @param clientId
     * @return
     */
    List<AppointmentEntity> getClientAppointments(Long clientId);

    /**
     * Get appointments by client.
     * @param masterId
     * @return
     */
    List<AppointmentEntity> getMasterAppointments(Long masterId);

    void switchStatus(AppointmentSwitchDto dto);

    AppointmentCreateGetModel getModel();

    List<AppointmentEntity> getAll();

    void delete (AppointmentSwitchDto dto);
}
