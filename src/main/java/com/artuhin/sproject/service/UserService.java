package com.artuhin.sproject.service;

import com.artuhin.sproject.model.dto.RegistrationDto;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.model.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {

    /**
     * Get all users.
     * @return
     */
    List<UserEntity> getAll();

    /**
     * Find user by ID.
     * @param id
     * @return
     */
    UserEntity getUserById(Long id);

    /**
     * Update user.
     * @param id
     * @param updateWith
     * @return
     */
    UserEntity updateUser(Long id, UserEntity updateWith);

    /**
     * Get masters.
     * @return
     */
    List<UserEntity> getMasters();

    /**
     * Get all masters group by specialization and sort by rating.
     * @return
     */
    Map<ProcedureEntity, List<UserEntity>> getMastersSpecializationRatings ();

    boolean saveUser(RegistrationDto user);
}
