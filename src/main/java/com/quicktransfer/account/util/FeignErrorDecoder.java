package com.quicktransfer.account.util;

import com.quicktransfer.account.exceptions.TransactionException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

/**
 * A custom error decoder for handling Feign client error responses.
 * <p>
 * This class implements the {@link ErrorDecoder} interface to provide custom error handling
 * for Feign client responses. It specifically handles cases where the response status is
 * {@code HttpStatus.BAD_REQUEST} and maps it to a {@link TransactionException}.
 * For other HTTP statuses, it delegates the error decoding to the default error decoder.
 * </p>
 */
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    /**
     * Decodes an error response from a Feign client call.
     * <p>
     * This method inspects the HTTP status of the response. If the status is {@code 400 Bad Request},
     * it creates and returns a {@link TransactionException} with an appropriate error message.
     * For all other statuses, it uses the default error decoder to handle the error.
     * </p>
     *
     * @param methodKey the key for the method that caused the error. This is used to identify
     *                  which Feign client method resulted in the error.
     * @param response  the Feign response object containing the HTTP status and error details.
     * @return an exception representing the error, such as a {@link TransactionException}
     * for bad requests, or an exception generated by the default error decoder for other cases.
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.BAD_REQUEST.value()) {
            return new TransactionException("Bad Request: " + response.reason());
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}