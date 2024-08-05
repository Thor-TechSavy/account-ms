package com.quicktransfer.account.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quicktransfer.account.dto.RequestIdentifierDto;
import com.quicktransfer.account.dto.RequestTransactionDto;
import com.quicktransfer.account.dto.TransactionDetailsDto;
import com.quicktransfer.account.entity.TransactionEntity;
import com.quicktransfer.account.enums.TransactionStatus;
import com.quicktransfer.account.exceptions.TransactionException;
import com.quicktransfer.account.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionMapperTest {

    @Test
    void testMapToEntity() throws JsonProcessingException {
        // Set up the RequestTransactionDto
        RequestTransactionDto requestDto = new RequestTransactionDto();
        requestDto.setFromOwnerId(UUID.randomUUID());
        requestDto.setToOwnerId(UUID.randomUUID());
        requestDto.setAmount(new BigDecimal("100.00"));

        RequestIdentifierDto identifierDto = new RequestIdentifierDto();
        identifierDto.setCalleeName("FUND-MS");
        identifierDto.setRequestTime(String.valueOf(Instant.now()));
        identifierDto.setTransferRequestId(UUID.fromString("775bbf93-8082-439e-89d5-e585224de199"));

        requestDto.setRequestIdentifier(identifierDto);

        // Map RequestTransactionDto to TransactionEntity
        TransactionEntity transactionEntity = TransactionMapper.mapToEntity(requestDto);

        // Verify the mappings
        assertEquals(requestDto.getFromOwnerId(), transactionEntity.getFromOwnerId());
        assertEquals(requestDto.getToOwnerId(), transactionEntity.getToOwnerId());
        assertEquals(requestDto.getAmount(), transactionEntity.getTxnAmt());
        assertEquals(JsonUtil.getMapper().writeValueAsString(requestDto.getRequestIdentifier()), transactionEntity.getRequestIdentifier());
    }

    @Test
    void testMapToEntityJsonProcessingException() {
        // Set up the RequestTransactionDto with invalid data
        RequestTransactionDto requestDto = new RequestTransactionDto() {
            @Override
            public RequestIdentifierDto getRequestIdentifier() {
                throw new TransactionException("Test exception");
            }
        };

        // Verify the exception is thrown
        assertThrows(TransactionException.class, () -> TransactionMapper.mapToEntity(requestDto));
    }

    @Test
    void testMapToDto() {
        // Set up the TransactionEntity
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setFromOwnerId(UUID.randomUUID());
        transactionEntity.setToOwnerId(UUID.randomUUID());
        transactionEntity.setCreditOperation(new BigDecimal("100.00"));
        transactionEntity.setRequestIdentifier(UUID.randomUUID().toString());
        transactionEntity.setStatus(TransactionStatus.SUCCESSFUL);

        // Map TransactionEntity to TransactionDetailsDto
        TransactionDetailsDto detailsDto = TransactionMapper.mapToDto(transactionEntity);

        // Verify the mappings
        assertEquals(transactionEntity.getFromOwnerId(), detailsDto.getFromOwnerId());
        assertEquals(transactionEntity.getToOwnerId(), detailsDto.getToOwnerId());
        assertEquals(transactionEntity.getCreditOperation(), detailsDto.getAmount());
        assertEquals(transactionEntity.getTransactionUUID(), detailsDto.getTransactionId());
        assertEquals(transactionEntity.getStatus(), detailsDto.getTransactionStatus());
        assertEquals(transactionEntity.getRequestIdentifier(), detailsDto.getRequestIdentifier());
    }
}
