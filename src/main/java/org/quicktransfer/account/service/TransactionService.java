package org.quicktransfer.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.quicktransfer.account.dto.RequestTransactionDto;
import org.quicktransfer.account.entity.AccountEntity;
import org.quicktransfer.account.entity.RequestIdentifier;
import org.quicktransfer.account.entity.TransactionEntity;
import org.quicktransfer.account.enums.TransactionStatus;
import org.quicktransfer.account.exceptions.AccountNotFoundException;
import org.quicktransfer.account.exceptions.InsufficientAccountBalanceException;
import org.quicktransfer.account.exceptions.TransactionException;
import org.quicktransfer.account.repository.TransactionRepository;
import org.quicktransfer.account.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Transactional
    public void creditAndDebitOperation(RequestTransactionDto transactionDto) {

        TransactionEntity transaction = createTransaction(transactionDto);

        try {
            AccountEntity fromAccount = accountService.findAccountByOwnerId(transactionDto.getFromOwnerId());
            AccountEntity toAccount = accountService.findAccountByOwnerId(transactionDto.getToOwnerId());

            validateDebitAccountBalance(
                    transactionDto.getAmount(),
                    fromAccount.getBalance().getAmount(),
                    transactionDto.getFromOwnerId());

            BigDecimal amountAfterDebit = fromAccount.getBalance().getAmount().subtract(transactionDto.getAmount());
            fromAccount.getBalance().setAmount(amountAfterDebit);
            accountService.updateAccount(fromAccount);

            BigDecimal amountAfterCredit = toAccount.getBalance().getAmount().add(transactionDto.getAmount());
            toAccount.getBalance().setAmount(amountAfterCredit);
            accountService.updateAccount(toAccount);

            transaction.setStatus(TransactionStatus.SUCCESSFUL);

        } catch (InsufficientAccountBalanceException | AccountNotFoundException e) {
            transaction.setStatus(TransactionStatus.FAILED);
            update(transaction);

        } finally {
            update(transaction);

        }
    }

    private static void validateDebitAccountBalance(BigDecimal debitAmount, BigDecimal availableBalance, UUID ownerId) {
        if (availableBalance.compareTo(debitAmount) < 0) {
            throw new InsufficientAccountBalanceException("Account doesn't have enough balance, ownerId: " + ownerId);
        }
    }


    @Transactional(propagation = Propagation.MANDATORY)
    public TransactionEntity createTransaction(RequestTransactionDto transactionDto) {

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setFromOwnerId(transactionDto.getFromOwnerId());
        transactionEntity.setToOwnerId(transactionDto.getToOwnerId());
        transactionEntity.setDebitOperation(transactionDto.getAmount());
        transactionEntity.setCreditOperation(transactionDto.getAmount());

        RequestIdentifier identifier = new RequestIdentifier();
        identifier.setCalleeName(transactionDto.getCalleeName());
        identifier.setRequestTime(transactionDto.getTransferRequestCreationTime());
        identifier.setTransferRequestId(transactionDto.getTransferRequestUUID());

        try {
            String jsonIdentifier = JsonUtil.getMapper().writeValueAsString(identifier);
            transactionEntity.setRequestIdentifier(jsonIdentifier);
        } catch (JsonProcessingException e) {
            throw new TransactionException(e.getMessage());
        }
        transactionEntity.setStatus(TransactionStatus.IN_PROGRESS);
        return transactionRepository.save(transactionEntity);
    }

    public TransactionEntity findTransactionByTransactionUUID(UUID transactionUUID) {

        return transactionRepository.findByTransactionUUID(transactionUUID)
                .orElseThrow(() -> new TransactionException("not found for uuid: " + transactionUUID));


    }

    public void update(TransactionEntity transactionEntity) {

        transactionEntity.setLastUpdateTime(Instant.now());
        transactionRepository.save(transactionEntity);
    }


}
