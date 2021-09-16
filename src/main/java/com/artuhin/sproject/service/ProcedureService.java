package com.artuhin.sproject.service;

import com.artuhin.sproject.model.entity.ProcedureEntity;

import java.util.List;

public interface ProcedureService {

    /**
     * Find all procedures.
     * @return
     */
    List<ProcedureEntity> getAll();

    /**
     * Get procedure by id.
     * @param id
     * @return
     */
    ProcedureEntity getProcedureById(Long id);

    ProcedureEntity getProcedureByName (String name);
}
