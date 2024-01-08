package org.national.transfer.backoffice.service.exception;

import lombok.Getter;

public class TransferException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    private Integer status;

    public TransferException() {
        super();
    }

    public TransferException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransferException(String message) {
        super(message);
    }

    public TransferException(String message, int status) {
        super(message);
        this.status = status;
    }
}