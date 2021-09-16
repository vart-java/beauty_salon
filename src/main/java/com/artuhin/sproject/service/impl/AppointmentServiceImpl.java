package com.artuhin.sproject.service.impl;

import com.artuhin.sproject.exception.MasterCanNotPerformProcedureException;
import com.artuhin.sproject.exception.ProcedureCanNotBeArrangedException;
import com.artuhin.sproject.model.AppointmentStatus;
import com.artuhin.sproject.model.dto.AppointmentSwitchDto;
import com.artuhin.sproject.model.dto.CreateAppointmentPostDto;
import com.artuhin.sproject.repository.AppointmentRepository;
import com.artuhin.sproject.service.UserService;
import com.artuhin.sproject.model.AppointmentCreateGetModel;
import com.artuhin.sproject.model.entity.AppointmentEntity;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.model.entity.UserEntity;
import com.artuhin.sproject.service.AppointmentService;
import com.artuhin.sproject.service.ProcedureService;
import com.artuhin.sproject.util.ExceptionMessageTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private UserService userService;
    @Autowired
    private ProcedureService procedureService;
    @Autowired
    private AppointmentRepository appointmentRepository;


    @Override
    public AppointmentEntity createAppointment(CreateAppointmentPostDto dto) {
        UserEntity client = userService.getUserById(dto.getClientId());
        UserEntity master = userService.getUserById(dto.getMasterId());
        ProcedureEntity procedure = procedureService.getProcedureByName(dto.getProcedureName());
        Timestamp start = dto.getStartDate();
        Timestamp end = getEndDateOfProcedure(start, procedure.getDuration());
        checkIfMasterCanPerformThisProcedure(master, procedure);
        checkIfMasterIsFreeThisTime(master, start, end);
        AppointmentEntity newAppointment = AppointmentEntity.builder()
                .client(client)
                .master(master)
                .procedure(procedure)
                .startTime(start)
                .endTime(end)
                .appointmentStatus(AppointmentStatus.DECLARED)
                .build();
        return appointmentRepository.save(newAppointment);
    }

    @Override
    public List<AppointmentEntity> getClientAppointments(Long clientId) {
        UserEntity client = userService.getUserById(clientId);
        return appointmentRepository.findAllByClient(client);
    }

    @Override
    public List<AppointmentEntity> getMasterAppointments(Long masterId) {
        UserEntity master = userService.getUserById(masterId);
        return appointmentRepository.findAllByMaster(master);
    }

    @Override
    public void switchStatus(AppointmentSwitchDto dto) {
        AppointmentStatus appointmentStatus = appointmentRepository.getById(dto.getId()).getAppointmentStatus();
        for (AppointmentStatus as : AppointmentStatus.values()) {
            if (appointmentStatus.ordinal() + 1 == as.ordinal()) {
                appointmentStatus = as;
            }
        }
        AppointmentEntity appointment = appointmentRepository.getById(dto.getId());
        appointment.setAppointmentStatus(appointmentStatus);
        if (appointmentRepository.save(appointment).getAppointmentStatus() == null) {
            throw new ProcedureCanNotBeArrangedException(String.format(ExceptionMessageTemplates.MASTER_CAN_NOT_PERFORM_PROCEDURE_MESSAGE, dto.getId()));
        }
    }

    @Override
    public List<AppointmentEntity> getAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public void delete(AppointmentSwitchDto dto) {
        appointmentRepository.deleteById(dto.getId());
    }

    public AppointmentCreateGetModel getModel() {
        List<UserEntity> masters = userService.getMasters();
        List<ProcedureEntity> procedures = procedureService.getAll();
        return AppointmentCreateGetModel.builder()
                .masters(masters)
                .procedures(procedures)
                .build();
    }


    private Timestamp getEndDateOfProcedure(Timestamp startDate, Duration duration) {
        return Timestamp.valueOf(startDate.toLocalDateTime().plusMinutes(duration.toMinutes()));
    }

    private void checkIfMasterCanPerformThisProcedure(UserEntity master, ProcedureEntity procedure) {
        long countOfProcedures = master.getSkills().stream().filter(p -> procedure.getName().equals(p.getName())).count();
        if (countOfProcedures < 1) {
            throw new MasterCanNotPerformProcedureException(String.format(ExceptionMessageTemplates.MASTER_CAN_NOT_PERFORM_PROCEDURE_MESSAGE, procedure.getName()));
        }
    }

    private void checkIfMasterIsFreeThisTime(UserEntity master, Timestamp start, Timestamp end) {
        int startHour = start.toLocalDateTime().getHour();
        int endHour = end.toLocalDateTime().getHour();
        long countOfCollisions = appointmentRepository.findAllByMaster(master).stream()
                .filter(a -> (a.getStartTime().after(start) && a.getStartTime().before(end) ||
                        (a.getEndTime().after(start) && a.getEndTime().before(end))) ||
                        a.getStartTime().equals(start) ||
                        a.getEndTime().equals(end)).count();
        if (start.toLocalDateTime().isBefore(LocalDateTime.now()) || startHour < 8 || endHour > 20 || countOfCollisions > 0) {
            throw new ProcedureCanNotBeArrangedException(ExceptionMessageTemplates.PROCEDURE_CAN_NOT_BE_ARRANGED_MESSAGE);
        }
    }

}
