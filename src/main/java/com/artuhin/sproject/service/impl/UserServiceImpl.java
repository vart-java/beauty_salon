package com.artuhin.sproject.service.impl;

import com.artuhin.sproject.exception.UserCanNotBeUpdatedException;
import com.artuhin.sproject.exception.UsersNotFoundException;
import com.artuhin.sproject.model.Role;
import com.artuhin.sproject.model.dto.RegistrationDto;
import com.artuhin.sproject.service.UserService;
import com.artuhin.sproject.exception.UserNotFoundException;
import com.artuhin.sproject.model.entity.UserEntity;
import com.artuhin.sproject.repository.UserRepository;
import com.artuhin.sproject.util.ExceptionMessageTemplates;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessageTemplates.USER_NOT_FOUND_MESSAGE));
    }

    public boolean saveUser(RegistrationDto registrationDto) {
        UserEntity userFromDB = userRepository.findUserEntityByLogin(registrationDto.getLogin());

        if (userFromDB != null) {
            return false;
        }
        UserEntity newUser = new UserEntity();
        newUser.setLogin(registrationDto.getLogin());
        newUser.setRole(Role.CLIENT);
        newUser.setPassword(bCryptPasswordEncoder.encode(registrationDto.getPassword()));
        userRepository.save(newUser);
        return true;
    }

    @Override
    public void updateRating(long masterId, int rating) {
        UserEntity master = getUserById(masterId);
        int recallCount = master.getRecallCount();
        double masterRating = master.getRating();
        if (masterRating == 0 || recallCount == 0) {
            master.setRating(rating);
            master.setRecallCount(1);
        } else {
            master.setRating(Precision.round(((masterRating * recallCount + rating) / (recallCount + 1)), 2));
            master.setRecallCount(recallCount + 1);
        }
        userRepository.save(master);
    }

    @Override
    public UserEntity updateUser(Long id, UserEntity updateWith) {
        UserEntity userToUpdate = getUserById(id);
        Optional<UserEntity> updateWithOpt = Optional.of(updateWith);
        updateWithOpt.map(UserEntity::getLogin)
                .ifPresent(userToUpdate::setLogin);
        updateWithOpt.map(UserEntity::getPassword)
                .ifPresent(userToUpdate::setPassword);
        updateWithOpt.map(UserEntity::getRole)
                .ifPresent(userToUpdate::setRole);
        updateWithOpt.map(UserEntity::getRating)
                .ifPresent(userToUpdate::setRating);
        updateWithOpt.map(UserEntity::getRecallCount)
                .ifPresent(userToUpdate::setRecallCount);
        updateWithOpt.map(UserEntity::getSkills)
                .ifPresent(userToUpdate::setSkills);
        try {
            return userRepository.save(userToUpdate);
        } catch (Exception e) {
            throw new UserCanNotBeUpdatedException(ExceptionMessageTemplates.USER_CAN_NOT_BE_UPDATED_MESSAGE);
        }
    }

    @Override
    public List<UserEntity> getMasters() {
        try {
            return getAll().stream().filter(userEntity -> ObjectUtils.isNotEmpty(userEntity.getSkills()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new UsersNotFoundException(ExceptionMessageTemplates.USERS_NOT_FOUND_MESSAGE);
        }
    }

//    ask about how this shit is working
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserEntityByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
