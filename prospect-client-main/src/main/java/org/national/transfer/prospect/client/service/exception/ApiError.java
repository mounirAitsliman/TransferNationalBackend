package org.national.transfer.prospect.client.service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(Include.NON_NULL)
@Getter
public class ApiError implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long timestamp = new Date().getTime();
    private HttpStatus status;
    private String message;
    private String error;
    @Setter
    private String transactionId;

    public ApiError(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        this.error = error;
    }
}
