package org.quicktransfer.account.service;

import org.quicktransfer.account.dto.CreateAccountDto;
import org.quicktransfer.account.dto.UpdateAccountDto;
import org.quicktransfer.account.entity.AccountEntity;
import org.quicktransfer.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        accountEntity.setBalance(accountDto.getBalance());

        return accountRepository.save(accountEntity);

    }

    public AccountEntity getAccount(UUID ownerId) {
        return accountRepository.findByOwnerId(ownerId);
    }

    public AccountEntity updateAccount(UUID ownerId, UpdateAccountDto accountDto) {

        AccountEntity account = getAccount(ownerId);
        account.setBalance(accountDto.getBalance());

        return accountRepository.save(account);
    }

}
