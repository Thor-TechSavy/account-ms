package com.quicktransfer.account.exceptions;

public class ExchangeRateException extends RuntimeException {

    private static final long serialVersionUID = -2860968399524171926L;

    /**
     * Constructor with the error message
     *
     * @param errormessage The error message
     */
    public ExchangeRateException(String errormessage) {
        super(errormessage);
    }

    /**
     * Constructor with the error message and the origin exception.
     *
     * @param errorMessage The error message.
     * @param throwable    The origin exception
     */
    public ExchangeRateException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
