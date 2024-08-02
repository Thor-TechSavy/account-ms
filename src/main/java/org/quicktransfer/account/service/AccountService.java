package org.quicktransfer.account.service;

import org.quicktransfer.account.dto.CreateAccountDto;
import org.quicktransfer.account.entity.AccountEntity;
import org.quicktransfer.account.entity.BalanceEntity;
import org.quicktransfer.account.exceptions.AccountNotFoundException;
import org.quicktransfer.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountEntity createAccount(CreateAccountDto accountDto) {

        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setCurrency(accountDto.getCurrency());
        accountEntity.setFirstName(accountDto.getFirstName());
        accountEntity.setLastName(accountDto.getLastName());
        accountEntity.setDob(accountDto.getDob());

        BalanceEntity balanceEntity = new BalanceEntity();
        balanceEntity.setAmount(new BigDecimal(0));
        balanceEntity.setAccount(accountEntity);
        balanceEntity.setLastUpdate(Instant.now());

        accountEntity.setBalance(balanceEntity);

        return accountRepository.save(accountEntity);


    }

    @Transactional
    public AccountEntity findAccountByOwnerId(UUID ownerId) throws AccountNotFoundException {
        return accountRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new AccountNotFoundException("Account doesn't exist for owner id: " + ownerId));
    }

    @Transactional
    public void updateAccount(AccountEntity accountEntity) {

        accountEntity.setLastUpdateTime(Instant.now());

        accountRepository.save(accountEntity);
    }


}
