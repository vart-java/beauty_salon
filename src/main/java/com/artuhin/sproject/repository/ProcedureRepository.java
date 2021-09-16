package com.artuhin.sproject.repository;

import com.artuhin.sproject.model.entity.ProcedureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcedureRepository extends JpaRepository<ProcedureEntity, Long> {
}
