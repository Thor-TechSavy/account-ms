package org.quicktransfer.account.controller;

import org.quicktransfer.account.dto.RequestTransactionDto;
import org.quicktransfer.account.dto.TransactionDetailsDto;
import org.quicktransfer.account.entity.TransactionEntity;
import org.quicktransfer.account.service.TransactionService;
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
    public ResponseEntity<Void> creditAndDebitOperations(@RequestBody RequestTransactionDto transactionDto) {

        transactionService.creditAndDebitOperation(transactionDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{transactionUUID}")
    public ResponseEntity<TransactionDetailsDto> findTransactionByUUID(@PathVariable UUID transactionUUID) {

        TransactionEntity entity = transactionService.findTransactionByTransactionUUID(transactionUUID);
        TransactionDetailsDto detailsDto = new TransactionDetailsDto();
        detailsDto.setAmount(entity.getCreditOperation());
        detailsDto.setFromOwnerId(entity.getFromOwnerId());
        detailsDto.setToOwnerId(entity.getToOwnerId());
        detailsDto.setTransactionId(entity.getTransactionUUID());
        detailsDto.setTransactionStatus(entity.getStatus());
        detailsDto.setRequestIdentifier(entity.getRequestIdentifier());

        return new ResponseEntity<>(detailsDto, HttpStatus.OK);
    }
}
