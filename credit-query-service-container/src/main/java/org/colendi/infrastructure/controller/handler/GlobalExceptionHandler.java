package org.colendi.infrastructure.controller.handler;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.colendi.domain.exception.CreditDomainException;
import org.colendi.usecase.exception.CreditApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {CreditDomainException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(CreditDomainException creditDomainException) {
        return ErrorDTO.builder()
            .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(creditDomainException.getMessage())
            .fieldErrors(new ArrayList<>())
            .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {CreditApplicationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(CreditApplicationException creditApplicationException) {
        return ErrorDTO.builder()
            .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(creditApplicationException.getMessage())
            .fieldErrors(new ArrayList<>())
            .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(HttpMessageNotReadableException httpMessageNotReadableException) {
        return ErrorDTO.builder()
            .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message("Wrong date type it should be yyyy-MM-dd'T'HH:mm:ss.SSS")
            .fieldErrors(new ArrayList<>())
            .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(MethodArgumentNotValidException methodArgumentNotValidException) {
        List<String> fieldErrors = methodArgumentNotValidException.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();

        return ErrorDTO.builder()
            .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message("Validation failed for one or more fields")
            .fieldErrors(fieldErrors)
            .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDTO.builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .message("Unexpected server error")
            .build();
    }
}
