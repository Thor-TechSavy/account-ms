package com.quicktransfer.account.enums;

public enum TransactionStatus {

    SUCCESSFUL("successful"),
    PROCESSING("processing"),
    FAILED("failed");

    private final String value;

    TransactionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
