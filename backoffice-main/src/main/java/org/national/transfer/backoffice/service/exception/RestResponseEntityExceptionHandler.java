package org.national.transfer.backoffice.service.exception;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@CommonsLog
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {org.national.transfer.backoffice.service.exception.NotFoundException.class})
    protected ResponseEntity<Object> notFoundException(RuntimeException ex, WebRequest request) {
        log.error(ex, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new org.national.transfer.backoffice.service.exception.ApiError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(value = {TransferException.class})
    protected ResponseEntity<Object> transferExceprion(RuntimeException ex, WebRequest request) {
        log.error(ex, ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new org.national.transfer.backoffice.service.exception.ApiError(HttpStatus.CONFLICT, ex.getMessage()));
    }
}