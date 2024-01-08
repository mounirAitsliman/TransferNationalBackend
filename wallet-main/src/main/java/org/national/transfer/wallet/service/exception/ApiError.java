package org.national.transfer.wallet.service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import org.national.transfer.wallet.service.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
        addTransactionId();
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        this.error = error;
        addTransactionId();
    }

    private void addTransactionId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        this.transactionId = (String) request.getAttribute(Constants.TRANSACTION_ID);
    }
}
