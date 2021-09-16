package com.artuhin.sproject.repository;

import com.artuhin.sproject.model.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository <RoleEntity, Long> {
}
