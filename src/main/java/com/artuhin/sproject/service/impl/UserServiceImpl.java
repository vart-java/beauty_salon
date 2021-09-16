package com.artuhin.sproject.service.impl;

import com.artuhin.sproject.model.dto.RegistrationDto;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.model.entity.RoleEntity;
import com.artuhin.sproject.service.ProcedureService;
import com.artuhin.sproject.service.UserService;
import com.artuhin.sproject.exception.UserCanNotBeUpdatedException;
import com.artuhin.sproject.exception.UserNotFoundException;
import com.artuhin.sproject.model.entity.UserEntity;
import com.artuhin.sproject.repository.UserRepository;
import com.artuhin.sproject.util.ExceptionMessageTemplates;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private ProcedureService procedureService;

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
        newUser.setRoles(Collections.singleton(RoleEntity.builder().id(3L).name("CLIENT").build()));
        newUser.setPassword(bCryptPasswordEncoder.encode(registrationDto.getPassword()));
        userRepository.save(newUser);
        return true;
    }

    @Override
    public UserEntity updateUser(Long id, UserEntity updateWith) {
        UserEntity userToUpdate = getUserById(id);
        Optional<UserEntity> updateWithOpt = Optional.of(updateWith);
        updateWithOpt.map(UserEntity::getLogin)
                .ifPresent(userToUpdate::setLogin);
        updateWithOpt.map(UserEntity::getPassword)
                .ifPresent(userToUpdate::setPassword);
        updateWithOpt.map(UserEntity::getRoles)
                .ifPresent(userToUpdate::setRoles);
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
        return getAll().stream().filter(userEntity -> ObjectUtils.isNotEmpty(userEntity.getSkills()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<ProcedureEntity, List<UserEntity>> getMastersSpecializationRatings() {
        Map<ProcedureEntity, List<UserEntity>> ratingMap = new HashMap<>();
        procedureService.getAll().forEach(p -> ratingMap.put(p, getMasters().stream().filter(m -> m.getSkills().stream().filter(s->s.equals(p)).findAny().isPresent()).sorted(Comparator.comparingDouble(UserEntity::getRating).reversed()).collect(Collectors.toList())));
        return ratingMap;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserEntityByLogin(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}
