package org.quicktransfer.account.enums;

public enum TransactionStatus {

    SUCCESSFUL("successful"),
    IN_PROGRESS("inProgress"),
    FAILED("failed");

    private final String value;

    TransactionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
