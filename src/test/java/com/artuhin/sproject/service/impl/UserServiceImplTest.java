package com.artuhin.sproject.service.impl;

import com.artuhin.sproject.model.Role;
import com.artuhin.sproject.model.dto.RegistrationDto;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.model.entity.UserEntity;
import com.artuhin.sproject.repository.UserRepository;
import com.artuhin.sproject.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Captor
    ArgumentCaptor<UserEntity> userCaptor;

    private UserEntity clientEntity;
    private UserEntity masterEntity;
    private RegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        clientEntity = UserEntity
                .builder()
                .id(1L)
                .login("login1")
                .password("password1")
                .role(Role.CLIENT)
                .build();

        masterEntity = UserEntity
                .builder()
                .id(2L)
                .login("login2")
                .password("password2")
                .rating(6)
                .recallCount(1)
                .role(Role.MASTER)
                .skills(Set.of(ProcedureEntity
                        .builder()
                        .id(1L)
                        .name("hair_coloring")
                        .duration(Duration.ZERO)
                        .build()))
                .build();

        registrationDto = RegistrationDto.builder()
                .login(masterEntity.getLogin())
                .password(masterEntity.getPassword())
                .build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(clientEntity));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(masterEntity));
        Mockito.when(userRepository.findAll()).thenReturn(List.of(clientEntity, masterEntity));
        Mockito.when(userRepository.findUserEntityByLogin(masterEntity.getLogin())).thenReturn(null);
        Mockito.when(userRepository.findUserEntityByLogin(clientEntity.getLogin())).thenReturn(clientEntity);
        Mockito.when(userRepository.save(any(UserEntity.class))).thenReturn(masterEntity);
    }

    @Test
    void getAll() {
        List<UserEntity> userEntities = userService.getAll();
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(userEntities, List.of(clientEntity, masterEntity));
    }

    @Test
    void getUserById() {
        UserEntity user = userService.getUserById(clientEntity.getId());
        Mockito.verify(userRepository, Mockito.times(1)).findById(clientEntity.getId());
        Assertions.assertEquals(user, clientEntity);
    }

    @Test
    void saveUser() {
        Boolean b = userService.saveUser(registrationDto);
        Mockito
                .verify(userRepository, Mockito.times(1))
                .findUserEntityByLogin(masterEntity.getLogin());
        UserEntity newUser = new UserEntity();
        newUser.setLogin(registrationDto.getLogin());
        newUser.setRole(Role.CLIENT);
        newUser.setPassword(bCryptPasswordEncoder.encode(registrationDto.getPassword()));
        Mockito.verify(userRepository, Mockito.times(1)).save(newUser);
    }

    @Test
    void updateRating() {
        userService.updateRating(masterEntity.getId(), 10);
        Mockito.verify(userRepository).save(userCaptor.capture());
        UserEntity actualUser = userCaptor.getValue();
        Assertions.assertEquals(8, actualUser.getRating());
    }

    @Test
    void updateUser() {
        UserEntity updatedUser = userService.updateUser(clientEntity.getId(), masterEntity);
        Mockito.verify(userRepository, Mockito.times(1)).findById(clientEntity.getId());
        masterEntity.setId(clientEntity.getId());
        Mockito.verify(userRepository, Mockito.times(1)).save(masterEntity);
        Assertions.assertEquals(updatedUser, masterEntity);
    }

    @Test
    void getMasters() {
        List<UserEntity> userEntities = userService.getMasters();
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(userEntities, List.of(masterEntity));
    }

    @Test
    void loadUserByUsername() {
        UserDetails user = userService.loadUserByUsername(clientEntity.getLogin());
        Mockito.verify(userRepository, Mockito.times(1)).findUserEntityByLogin(clientEntity.getLogin());
        UserDetails user1 = clientEntity;
        Assertions.assertEquals(user1, user);
    }
}