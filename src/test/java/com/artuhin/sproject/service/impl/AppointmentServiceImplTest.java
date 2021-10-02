package com.artuhin.sproject.service.impl;

import com.artuhin.sproject.model.AppointmentStatus;
import com.artuhin.sproject.model.Role;
import com.artuhin.sproject.model.dto.AppointmentSwitchDto;
import com.artuhin.sproject.model.dto.CreateAppointmentPostDto;
import com.artuhin.sproject.model.entity.AppointmentEntity;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.model.entity.UserEntity;
import com.artuhin.sproject.repository.AppointmentRepository;
import com.artuhin.sproject.service.AppointmentService;
import com.artuhin.sproject.service.ProcedureService;
import com.artuhin.sproject.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(SpringRunner.class)
@SpringBootTest
class AppointmentServiceImplTest {
    @Autowired
    private AppointmentService appointmentService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProcedureService procedureService;

    @MockBean
    private AppointmentRepository appointmentRepository;

    private ProcedureEntity procedureEntity;
    private UserEntity clientEntity;
    private UserEntity masterEntity;
    private AppointmentEntity appointmentEntity;
    private CreateAppointmentPostDto createAppointmentPostDto;
    private AppointmentSwitchDto appointmentSwitchDto;

    @BeforeEach
    public void setUp() {
        clientEntity = UserEntity
                .builder()
                .id(1L)
                .login("login")
                .password("password")
                .role(Role.CLIENT)
                .build();

        masterEntity = UserEntity
                .builder()
                .id(2L)
                .login("login")
                .password("password")
                .rating(10)
                .recallCount(1)
                .role(Role.MASTER)
                .skills(Set.of(ProcedureEntity
                        .builder()
                        .id(1L)
                        .name("hair_coloring")
                        .duration(Duration.ZERO)
                        .build()))
                .build();

        procedureEntity = ProcedureEntity
                .builder()
                .id(1L)
                .name("hair_coloring")
                .masters(Set.of(masterEntity))
                .duration(Duration.ZERO)
                .build();

        createAppointmentPostDto = CreateAppointmentPostDto
                .builder()
                .clientId(1L)
                .masterId(2L)
                .startDate(Timestamp.valueOf("2021-10-10 11:05:00"))
                .procedureName("hair_coloring")
                .build();

        appointmentEntity = AppointmentEntity
                .builder()
                .id(1L)
                .client(clientEntity)
                .master(masterEntity)
                .procedure(procedureEntity)
                .startTime(Timestamp.valueOf("2021-10-10 11:05:00"))
                .appointmentStatus(AppointmentStatus.DECLARED)
                .build();

        appointmentSwitchDto = AppointmentSwitchDto
                .builder()
                .id(1L)
                .build();

        Mockito.when(procedureService.getProcedureByName("hair_coloring")).thenReturn(procedureEntity);
        Mockito.when(userService.getUserById(1L)).thenReturn(clientEntity);
        Mockito.when(userService.getUserById(2L)).thenReturn(masterEntity);
        Mockito.when(appointmentRepository.save(any(AppointmentEntity.class))).thenReturn(appointmentEntity);
        Mockito
                .when(appointmentRepository.findAllByClient(clientEntity))
                .thenReturn(Collections.singletonList(appointmentEntity));
        Mockito
                .when(appointmentRepository.findAllByClient(masterEntity))
                .thenReturn(Collections.singletonList(appointmentEntity));
        Mockito.when(appointmentRepository.getById(anyLong())).thenReturn(appointmentEntity);
        Mockito.when(appointmentRepository.findAll()).thenReturn(Collections.singletonList(appointmentEntity));
    }

    @Test
    void createAppointment() {
        AppointmentEntity appointmentEntity = appointmentService.createAppointment(createAppointmentPostDto);
        Assert.assertNotNull(appointmentEntity);
        Assert.assertNotNull(appointmentEntity.getId());
        Mockito
                .verify(userService, Mockito.times(1))
                .getUserById(createAppointmentPostDto.getClientId());
        Mockito
                .verify(userService, Mockito.times
                (1))
                .getUserById(createAppointmentPostDto.getMasterId());
        Mockito
                .verify(procedureService, Mockito.times(1))
                .getProcedureByName(createAppointmentPostDto.getProcedureName());
        Mockito.verify(appointmentRepository, Mockito.times(1)).save(any(AppointmentEntity.class));
    }

    @Test
    void getClientAppointments() {
        List<AppointmentEntity> appointmentEntityList = appointmentService.getClientAppointments(1L);
        Mockito.verify(userService, Mockito.times(1)).getUserById(1L);
        Mockito.verify(appointmentRepository, Mockito.times(1)).findAllByClient(clientEntity);
        Assert.assertEquals(appointmentEntityList, Collections.singletonList(appointmentEntity));
    }

    @Test
    void getMasterAppointments() {
        List<AppointmentEntity> appointmentEntityList = appointmentService.getClientAppointments(2L);
        Mockito.verify(userService, Mockito.times(1)).getUserById(2L);
        Mockito.verify(appointmentRepository, Mockito.times(1)).findAllByClient(masterEntity);
        Assert.assertEquals(appointmentEntityList, Collections.singletonList(appointmentEntity));
    }

    @Test
    void switchStatus() {
        appointmentService.switchStatus(appointmentSwitchDto);
        Mockito
                .verify(appointmentRepository, Mockito.times(2))
                .getById(appointmentSwitchDto.getId());
        appointmentEntity.setAppointmentStatus(AppointmentStatus.CONFIRMED);
        Mockito.verify(appointmentRepository, Mockito.times(1)).save(appointmentEntity);
    }

    @Test
    void getAll() {
        List<AppointmentEntity> appointmentEntities = appointmentService.getAll();
        Mockito.verify(appointmentRepository, Mockito.times(1)).findAll();
        Assert.assertEquals(Collections.singletonList(appointmentEntity), appointmentEntities);
    }

    @Test
    void delete() {
        appointmentService.delete(appointmentSwitchDto);
        Mockito
                .verify(appointmentRepository, Mockito.times(1))
                .deleteById(appointmentSwitchDto.getId());
    }

    @Test
    void updateTimeAppointment() {
        appointmentService.updateTimeAppointment(appointmentEntity.getId(), "11:05:00");
        Mockito.verify(appointmentRepository, Mockito.times(1)).getById(appointmentEntity.getId());
        appointmentEntity
                .setStartTime(Timestamp.valueOf(LocalDateTime.of(appointmentEntity
                        .getStartTime()
                        .toLocalDateTime()
                        .toLocalDate(), LocalTime.parse("11:05:00"))));
        Mockito.verify(appointmentRepository, Mockito.times(1)).save(appointmentEntity);
    }

    @Test
    void getDataForEmail() {
        List<AppointmentEntity> appointmentEntities = appointmentService
                .getDataForEmail(LocalDateTime.now().toLocalDate());
        Mockito.verify(appointmentRepository, Mockito.times(1)).findAll();
        Assert.assertTrue(appointmentEntities.isEmpty());
    }

    @Test
    void getById() {
        AppointmentEntity appointmentEntityById = appointmentService.getById(appointmentEntity.getId());
        Mockito.verify(appointmentRepository, Mockito.times(1)).getById(appointmentEntity.getId());
        Assert.assertEquals(appointmentEntity, appointmentEntityById);
    }
}