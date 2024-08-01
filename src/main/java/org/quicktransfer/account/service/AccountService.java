package org.quicktransfer.account.service;

import org.quicktransfer.account.dto.CreateAccountDto;
import org.quicktransfer.account.dto.UpdateAccountDto;
import org.quicktransfer.account.entity.AccountEntity;
import org.quicktransfer.account.entity.BalanceEntity;
import org.quicktransfer.account.exceptions.AccountNotFoundException;
import org.quicktransfer.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
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
        balanceEntity.setBalance(new BigDecimal(0));
        balanceEntity.setAccount(accountEntity);
        balanceEntity.setLastUpdate(Instant.now());

        accountEntity.setBalance(balanceEntity);

        return accountRepository.save(accountEntity);


    }

    public AccountEntity getAccount(UUID ownerId) {
        Optional<AccountEntity> account = accountRepository.findByOwnerId(ownerId);
        if (account.isPresent()) {
            return account.get();
        } else {
            throw new AccountNotFoundException("Account doesn't exist for owner id: " + ownerId);
        }
    }

    public void updateAccountBalance(UUID ownerId, BigDecimal amount) {

        AccountEntity account = getAccount(ownerId);

        //   BigDecimal updateAmount = account.getBalance().add(amount);
        // account.setBalance(updateAmount);

        accountRepository.save(account);
    }

}
