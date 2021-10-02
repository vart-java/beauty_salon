package com.artuhin.sproject.exception;

public class ProcedureNotFoundException extends RuntimeException {
    public ProcedureNotFoundException(String procedureNotFoundMessage) {
        super(procedureNotFoundMessage);
    }
}
