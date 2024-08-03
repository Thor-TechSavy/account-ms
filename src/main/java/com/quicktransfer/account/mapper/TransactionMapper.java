package com.quicktransfer.account.mapper;

import com.quicktransfer.account.dto.RequestTransactionDto;
import com.quicktransfer.account.dto.TransactionDetailsDto;
import com.quicktransfer.account.entity.TransactionEntity;
import com.quicktransfer.account.exceptions.TransactionException;
import com.quicktransfer.account.util.JsonUtil;


public class TransactionMapper {

    private TransactionMapper() {
    }

    public static TransactionEntity mapToEntity(RequestTransactionDto transactionDto) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setFromOwnerId(transactionDto.getFromOwnerId());
        transactionEntity.setToOwnerId(transactionDto.getToOwnerId());
        transactionEntity.setTxnAmt(transactionDto.getAmount());
        try {
            String jsonIdentifier = JsonUtil.getMapper().writeValueAsString(transactionDto.getRequestIdentifier());
            transactionEntity.setRequestIdentifier(jsonIdentifier);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new TransactionException(e.getMessage());
        }
        return transactionEntity;
    }

    public static TransactionDetailsDto mapToDto(TransactionEntity entity) {
        TransactionDetailsDto detailsDto = new TransactionDetailsDto();
        detailsDto.setAmount(entity.getCreditOperation());
        detailsDto.setFromOwnerId(entity.getFromOwnerId());
        detailsDto.setToOwnerId(entity.getToOwnerId());
        detailsDto.setTransactionId(entity.getTransactionUUID());
        detailsDto.setTransactionStatus(entity.getStatus());
        detailsDto.setRequestIdentifier(entity.getRequestIdentifier());
        return detailsDto;
    }
}
