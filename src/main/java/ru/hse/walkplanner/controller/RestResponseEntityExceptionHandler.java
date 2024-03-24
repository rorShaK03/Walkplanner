package ru.hse.walkplanner.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.hse.walkplanner.dto.ApiErrorResponse;
import ru.hse.walkplanner.exception.ClientErrorException;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Server error, try later\n";
        log.info("Server error: " + ex.getMessage());
        log.info("Server error, stack trace: " + Arrays.toString(ex.getStackTrace()));
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {ClientErrorException.class})
    protected ResponseEntity<Object> handleClientError(ClientErrorException ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .exceptionMessage(ex.getMessage())
                .description("")
                .build();

        HttpStatus status = HttpStatus.valueOf(ex.code);

        return handleExceptionInternal(ex, apiErrorResponse,
                new HttpHeaders(), status, request);
    }
}