package org.national.transfer.emission.service.exception;

import lombok.Getter;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    private Integer status;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, int status) {
        super(message);
        this.status = status;
    }
}
