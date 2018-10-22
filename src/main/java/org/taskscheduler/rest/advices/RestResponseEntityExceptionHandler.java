package org.taskscheduler.rest.advices;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.taskscheduler.domain.exceptions.EntityNotFoundException;
import org.taskscheduler.domain.exceptions.InvalidVerificationTokenException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<?> handleNotFoundException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = InvalidVerificationTokenException.class)
    protected ResponseEntity<Object> handleInvalidVerificationTokenException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Invalid Verification token", new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }
}
