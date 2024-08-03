package com.quicktransfer.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class RequestIdentifierDto {

    @Schema(description = "name of the caller", requiredMode = Schema.RequiredMode.REQUIRED, example = "ZEUS")
    private String calleeName;

    @Schema(description = "funds transfer request creation time", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-08-03T16:06:38.919517900Z")
    private String requestTime;

    @Schema(description = "funds transfer request id", requiredMode = Schema.RequiredMode.REQUIRED, example = "111111111111-1111-1111-1111-11111111")
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
