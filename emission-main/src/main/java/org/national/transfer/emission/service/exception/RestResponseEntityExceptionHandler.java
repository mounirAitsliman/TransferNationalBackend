package org.national.transfer.emission.service.exception;

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

    @ExceptionHandler(value = {AmountHighException.class})
    protected ResponseEntity<Object> amountHighException(RuntimeException ex, WebRequest request) {
        log.error(ex, ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> notFoundException(RuntimeException ex, WebRequest request) {
        log.error(ex, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(value = {TransferException.class})
    protected ResponseEntity<Object> transferExceprion(RuntimeException ex, WebRequest request) {
        log.error(ex, ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(HttpStatus.CONFLICT, ex.getMessage()));
    }
}