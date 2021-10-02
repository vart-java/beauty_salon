package com.artuhin.sproject.service.impl;

import com.artuhin.sproject.exception.ProcedureNotFoundException;
import com.artuhin.sproject.model.entity.ProcedureEntity;
import com.artuhin.sproject.repository.ProcedureRepository;
import com.artuhin.sproject.service.ProcedureService;
import com.artuhin.sproject.util.ExceptionMessageTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProcedureServiceImpl implements ProcedureService {

    @Autowired
    private ProcedureRepository procedureRepository;

    @Override
    public List<ProcedureEntity> getAll() {
        return procedureRepository.findAll();
    }

    @Override
    public ProcedureEntity getProcedureById(Long id) {
        return procedureRepository.findById(id)
                .orElseThrow(() -> new ProcedureNotFoundException(ExceptionMessageTemplates.PROCEDURE_NOT_FOUND_MESSAGE));
    }

    @Override
    public ProcedureEntity getProcedureByName(String name) {
        return procedureRepository.findByName(name).orElseThrow(() -> new ProcedureNotFoundException(ExceptionMessageTemplates.PROCEDURE_NOT_FOUND_MESSAGE));
    }
}
