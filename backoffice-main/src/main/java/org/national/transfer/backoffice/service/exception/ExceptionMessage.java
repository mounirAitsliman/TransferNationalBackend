package org.national.transfer.backoffice.service.exception;

import lombok.Data;

@Data
public class ExceptionMessage {
    private String timestamp;
    private String status;
    private String error;
    private String message;
    private String path;
    private String requestId;
}
