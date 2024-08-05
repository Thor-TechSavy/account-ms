package com.quicktransfer.account.service;

import com.quicktransfer.account.entity.AccountEntity;
import com.quicktransfer.account.entity.BalanceEntity;
import com.quicktransfer.account.exceptions.AccountNotFoundException;
import com.quicktransfer.account.repository.AccountRepository;
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

    /**
     * Creates a new {@link AccountEntity} with an initialized balance and saves it to the repository.
     * <p>
     * This method initializes a new {@link BalanceEntity} with a zero amount and associates it with
     * the provided {@link AccountEntity}. The balance's last update time is set to the current time.
     * The {@link AccountEntity} is then saved to the {@link AccountRepository}, which persists the account
     * and its balance in the data store.
     * </p>
     *
     * @param accountEntity the {@link AccountEntity} to be created and saved. The entity should contain
     *                      the account details but will have its balance initialized by this method.
     * @return the saved {@link AccountEntity} with its balance persisted in the repository.
     * The returned entity will include the generated ID and any other changes made during
     * the save operation.
     */
    public AccountEntity createAccount(AccountEntity accountEntity) {

        var balanceEntity = new BalanceEntity();
        balanceEntity.setAmount(new BigDecimal(0));
        balanceEntity.setAccount(accountEntity);
        balanceEntity.setLastUpdate(Instant.now());

        accountEntity.setBalance(balanceEntity);

        return accountRepository.save(accountEntity);


    }

    /**
     * Retrieves an {@link AccountEntity} by its owner ID.
     * <p>
     * This method searches for an account in the repository based on the provided owner ID. If an
     * account with the specified ID exists, it is returned. Otherwise, an {@link AccountNotFoundException}
     * is thrown, indicating that no account was found for the given owner ID.
     * </p>
     *
     * @param ownerId the unique identifier of the account owner. This ID is used to locate the
     *                corresponding {@link AccountEntity} in the repository.
     * @return the {@link AccountEntity} associated with the given owner ID.
     * @throws AccountNotFoundException if no account is found with the specified owner ID. This exception
     *                                  is thrown to indicate that the requested account does not exist.
     */
    @Transactional
    public AccountEntity getAccount(UUID ownerId) throws AccountNotFoundException {
        return accountRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new AccountNotFoundException("Account doesn't exist for owner id: " + ownerId));
    }

    /**
     * Updates an existing {@link AccountEntity} in the repository.
     * <p>
     * This method sets the current timestamp as the last update time of the provided account entity
     * and then saves the updated entity to the repository.
     * </p>
     *
     * @param accountEntity the {@link AccountEntity} to be updated. This entity should already exist
     *                      in the repository and must be provided with the updated information.
     *                      The {@code lastUpdateTime} field will be set to the current timestamp before
     *                      saving the entity.
     */
    public void updateAccount(AccountEntity accountEntity) {

        accountEntity.setLastUpdateTime(Instant.now());
        accountRepository.save(accountEntity);
    }

}
