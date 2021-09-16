package com.artuhin.sproject.repository;

import com.artuhin.sproject.model.entity.AppointmentEntity;
import com.artuhin.sproject.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    List<AppointmentEntity> findAllByMaster(UserEntity master);
    List<AppointmentEntity> findAllByClient(UserEntity client);
}
