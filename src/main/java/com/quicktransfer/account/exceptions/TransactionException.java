package com.quicktransfer.account.exceptions;

public class TransactionException extends RuntimeException {

    private static final long serialVersionUID = -5642623898783872373L;

    /**
     * Constructor with the error message
     * @param errormessage The error message
     */
    public TransactionException(String errormessage) {
        super(errormessage);
    }

    /**
     * Constructor with the error message and the origin exception.
     * @param errorMessage The error message.
     * @param throwable The origin exception
     */
    public TransactionException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
