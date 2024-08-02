package com.quicktransfer.account.controller;

import com.quicktransfer.account.entity.TransactionEntity;
import com.quicktransfer.account.service.TransactionService;
import com.quicktransfer.account.dto.RequestIdentifier;
import com.quicktransfer.account.dto.TransactionDetailsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/account/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDetailsDto> performDebitAndCreditOperations(@RequestBody RequestIdentifier transactionDto) {

        TransactionEntity entity = transactionService.creditAndDebitOperation(transactionDto);
        TransactionDetailsDto detailsDto = mapToDto(entity);

        return new ResponseEntity<>(detailsDto, HttpStatus.CREATED);
    }

    @GetMapping("/{transactionUUID}")
    public ResponseEntity<TransactionDetailsDto> findTransactionByUUID(@PathVariable UUID transactionUUID) {

        TransactionEntity entity = transactionService.findTransactionByTransactionUUID(transactionUUID);
        TransactionDetailsDto detailsDto = mapToDto(entity);

        return new ResponseEntity<>(detailsDto, HttpStatus.OK);
    }

    private static TransactionDetailsDto mapToDto(TransactionEntity entity) {
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
