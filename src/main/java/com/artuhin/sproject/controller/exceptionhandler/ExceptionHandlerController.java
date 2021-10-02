package com.artuhin.sproject.controller.exceptionhandler;

import com.artuhin.sproject.exception.*;
import com.artuhin.sproject.model.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> validationError(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        BindingResult bindingResult = ex.getBindingResult();
        bindingResult.getFieldErrors().forEach(fieldError -> errors.add(fieldError.getField()));
        return badRequest(new ErrorDto("VALIDATION_ERROR", errors));
    }

    @ExceptionHandler({UserNotFoundException.class, UserCanNotBeUpdatedException.class,
            ProcedureCanNotBeArrangedException.class, ProcedureNotFoundException.class,
            MasterCanNotPerformProcedureException.class})
    public ResponseEntity<ErrorDto> userNotFoundError(RuntimeException ex) {
        return badRequest(new ErrorDto(ex.getMessage(), Collections.emptyList()));
    }

    private ResponseEntity<ErrorDto> badRequest(ErrorDto error) {
        return ResponseEntity.badRequest().body(error);
    }
}