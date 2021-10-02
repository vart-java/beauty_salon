package com.artuhin.sproject.service;

import com.artuhin.sproject.model.dto.AppointmentSwitchDto;
import com.artuhin.sproject.model.dto.CreateAppointmentPostDto;
import com.artuhin.sproject.model.entity.AppointmentEntity;

import java.time.LocalDate;
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

    List<AppointmentEntity> getAll();

    void delete (AppointmentSwitchDto dto);

    void updateTimeAppointment(long id, String time);

    List<AppointmentEntity> getDataForEmail(LocalDate localDate);

    AppointmentEntity getById(long id);
}
