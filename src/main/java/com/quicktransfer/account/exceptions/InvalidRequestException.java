package com.quicktransfer.account.exceptions;

public class InvalidRequestException extends RuntimeException {

    private static final long serialVersionUID = -1205229011134289180L;

    /**
     * Constructor with the error message
     * @param errormessage The error message
     */
    public InvalidRequestException(String errormessage) {
        super(errormessage);
    }

    /**
     * Constructor with the error message and the origin exception.
     * @param errorMessage The error message.
     * @param throwable The origin exception
     */
    public InvalidRequestException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
