package com.quicktransfer.account.controller;

import com.quicktransfer.account.dto.AccountDetailsDto;
import com.quicktransfer.account.entity.TransactionEntity;
import com.quicktransfer.account.enums.TransactionStatus;
import com.quicktransfer.account.exceptions.TransactionException;
import com.quicktransfer.account.service.TransactionService;
import com.quicktransfer.account.dto.RequestTransactionDto;
import com.quicktransfer.account.dto.TransactionDetailsDto;
import com.quicktransfer.account.util.JsonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static com.quicktransfer.account.mapper.TransactionMapper.mapToDto;
import static com.quicktransfer.account.mapper.TransactionMapper.mapToEntity;

@RestController
@RequestMapping("/v1/account/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "To process the transaction for debit and credit operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "transaction is processed", content =
                    {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDetailsDto.class))}),
            @ApiResponse(responseCode = "400", description = "invalid request payload", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server error, the details are logged on "
                    + "backend", content = @Content)})
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<TransactionDetailsDto> performDebitAndCreditOperations(
            @RequestBody RequestTransactionDto transactionDto) {

        String requestIdentifier = getRequestIdentifier(transactionDto);

        ResponseEntity<TransactionDetailsDto> existingTransactionResponse =
                checkIfTransactionAlreadyProcessed(requestIdentifier);
        if (existingTransactionResponse != null) {
            return existingTransactionResponse;
        }

        TransactionEntity transactionEntity = mapToEntity(transactionDto);
        transactionEntity.setStatus(TransactionStatus.PROCESSING);

        TransactionEntity transaction = transactionService.createTransaction(transactionEntity);
        try {
            transaction = transactionService.processTransaction(transaction);
            TransactionDetailsDto detailsDto = mapToDto(transaction);
            return new ResponseEntity<>(detailsDto, HttpStatus.CREATED);
        } catch (TransactionException e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionService.update(transaction);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "To retrieve the transaction details by transaction id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "transaction fetched successfully", content =
                    {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDetailsDto.class))}),
            @ApiResponse(responseCode = "400", description = "invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server error, the details are logged on "
                    + "backend", content = @Content)})
    @GetMapping(value = "/{transactionUUID}", produces = "application/json")
    public ResponseEntity<TransactionDetailsDto> getTransaction(@PathVariable UUID transactionUUID) {

        TransactionEntity entity = transactionService.getTransaction(transactionUUID);
        TransactionDetailsDto detailsDto = mapToDto(entity);

        return new ResponseEntity<>(detailsDto, HttpStatus.OK);
    }

    private ResponseEntity<TransactionDetailsDto> checkIfTransactionAlreadyProcessed(String requestIdentifier) {

        Optional<TransactionEntity> existingTransaction = transactionService.getTransaction(requestIdentifier);
        if (existingTransaction.isPresent() && existingTransaction.get().getStatus() == TransactionStatus.SUCCESSFUL) {
            return new ResponseEntity<>(mapToDto(existingTransaction.get()), HttpStatus.OK);
        }
        return null;
    }

    private static String getRequestIdentifier(RequestTransactionDto transactionDto) {
        String requestIdentifier;
        try {
            requestIdentifier = JsonUtil.getMapper().writeValueAsString(transactionDto.getRequestIdentifier());
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new TransactionException(e.getMessage());
        }
        return requestIdentifier;
    }

}
