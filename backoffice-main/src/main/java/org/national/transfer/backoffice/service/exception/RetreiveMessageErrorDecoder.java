package org.national.transfer.backoffice.service.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

public class RetreiveMessageErrorDecoder implements ErrorDecoder {

    private ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        ExceptionMessage message = null;
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        switch (response.status()) {
            case 404:
                return new org.national.transfer.backoffice.service.exception.NotFoundException(message.getMessage() != null ? message.getMessage() : "Not found");
            case 409:
                return new TransferException(message.getMessage() != null ? message.getMessage() : "Transfer issue");
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
}
