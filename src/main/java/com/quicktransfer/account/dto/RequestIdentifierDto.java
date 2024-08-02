package com.quicktransfer.account.dto;

import java.util.UUID;

public class RequestIdentifierDto {

    private String calleeName;
    private String requestTime;
    private UUID transferRequestId;

    public String getCalleeName() {
        return calleeName;
    }

    public void setCalleeName(String calleeName) {
        this.calleeName = calleeName;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public UUID getTransferRequestId() {
        return transferRequestId;
    }

    public void setTransferRequestId(UUID transferRequestId) {
        this.transferRequestId = transferRequestId;
    }
}
