package com.quicktransfer.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public class RequestTransactionDto {

    @Schema(description = "uuid of the owner whose account is to be debited", requiredMode = Schema.RequiredMode.REQUIRED, example = "111111111111-1111-1111-1111-11111111")
    private UUID fromOwnerId;

    @Schema(description = "uuid of the owner whose account is to be credited", requiredMode = Schema.RequiredMode.REQUIRED, example = "111111111111-1111-1111-1111-11111111")
    private UUID toOwnerId;

    @Schema(description = "transaction amount", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private BigDecimal amount;

    @Schema(description = "identifier composed of callee name, fundsTransferRequestId, fundsTransferRequestCreationTime",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private RequestIdentifierDto requestIdentifier;

    public UUID getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(UUID fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public UUID getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(UUID toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public RequestIdentifierDto getRequestIdentifier() {
        return requestIdentifier;
    }

    public void setRequestIdentifier(RequestIdentifierDto requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }

    @Override
    public String toString() {
        return "RequestTransactionDto{" +
                "fromOwnerId=" + fromOwnerId +
                ", toOwnerId=" + toOwnerId +
                ", amount=" + amount +
                ", requestIdentifier=" + requestIdentifier +
                '}';
    }
}
