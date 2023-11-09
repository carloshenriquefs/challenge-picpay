package com.picpay.challenge.picpay.config;

import com.picpay.challenge.picpay.dtos.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.picpay.challenge.picpay.constants.Constants.*;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity threatDuplicatedEntry(DataIntegrityViolationException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(USUARIO_JA_CADASTRADO, STATUS_CODE_400);
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity threat404(DataIntegrityViolationException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity threatGeneralException(Exception exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), STATUS_CODE_500);
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }
}
