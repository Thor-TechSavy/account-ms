package com.quicktransfer.account.exceptions;

public class InsufficientAccountBalanceException extends TransactionException {

    private static final long serialVersionUID = 8841681170063045212L;

    /**
     * Constructor with the error message
     * @param errormessage The error message
     */
    public InsufficientAccountBalanceException(String errormessage) {
        super(errormessage);
    }

    /**
     * Constructor with the error message and the origin exception.
     * @param errorMessage The error message.
     * @param throwable The origin exception
     */
    public InsufficientAccountBalanceException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
