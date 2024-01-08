package org.national.transfer.emission.service.exception;

import lombok.Getter;

public class AmountHighException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    private Integer status;

    public AmountHighException() {
        super();
    }

    public AmountHighException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmountHighException(String message) {
        super(message);
    }

    public AmountHighException(String message, int status) {
        super(message);
        this.status = status;
    }
}
