package com.artuhin.sproject.service.impl;

import com.artuhin.sproject.model.Role;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.model.entity.UserEntity;
import com.artuhin.sproject.repository.ProcedureRepository;
import com.artuhin.sproject.service.ProcedureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProcedureServiceImplTest {
    @Autowired
    ProcedureService procedureService;
    @MockBean
    ProcedureRepository procedureRepository;

    private ProcedureEntity procedureEntity;
    private UserEntity masterEntity;

    @BeforeEach
    void setUp() {
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
                .masters(Set.of())
                .duration(Duration.ZERO)
                .build();
        Mockito.when(procedureRepository.findAll()).thenReturn(Collections.singletonList(procedureEntity));
        Mockito.when(procedureRepository.findById(anyLong())).thenReturn(Optional.of(procedureEntity));
        Mockito.when(procedureRepository.findByName(anyString())).thenReturn(Optional.of(procedureEntity));
    }

    @Test
    void getAll() {
        List<ProcedureEntity> procedureEntities = procedureService.getAll();
        Mockito.verify(procedureRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(Collections.singletonList(procedureEntity), procedureEntities);
    }

    @Test
    void getProcedureById() {
        ProcedureEntity procedureEntity1 = procedureService.getProcedureById(procedureEntity.getId());
        Mockito.verify(procedureRepository, Mockito.times(1)).findById(procedureEntity.getId());
        Assertions.assertEquals(procedureEntity, procedureEntity1);
    }

    @Test
    void getProcedureByName() {
        ProcedureEntity procedureEntity1 = procedureService.getProcedureByName(procedureEntity.getName());
        Mockito.verify(procedureRepository, Mockito.times(1)).findByName(procedureEntity.getName());
        Assertions.assertEquals(procedureEntity, procedureEntity1);
    }
}