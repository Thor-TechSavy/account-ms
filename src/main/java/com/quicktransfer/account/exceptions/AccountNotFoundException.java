package com.quicktransfer.account.exceptions;

public class AccountNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -8206361842162195593L;

    /**
     * Constructor with the error message
     * @param errormessage The error message
     */
    public AccountNotFoundException(String errormessage) {
        super(errormessage);
    }

    /**
     * Constructor with the error message and the origin exception.
     * @param errorMessage The error message.
     * @param throwable The origin exception
     */
    public AccountNotFoundException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
