package com.quicktransfer.account.service;

import com.quicktransfer.account.entity.BalanceEntity;
import com.quicktransfer.account.repository.BalanceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    /**
     * Retrieves the balance information associated with a specific account.
     * <p>
     * This method queries the repository to find a {@link BalanceEntity} based on the account owner's ID.
     * If no balance is found for the given owner ID, it throws a {@link RuntimeException}.
     *
     * @param ownerId The UUID of the account owner whose balance is to be retrieved.
     *                This value cannot be null and should correspond to an existing account.
     * @return A {@link BalanceEntity} representing the balance details of the specified account.
     * The returned entity contains information such as the current amount and the last update timestamp.
     * @throws RuntimeException if no balance is found for the specified owner ID.
     *                          This indicates that the requested account does not exist in the repository.
     */
    public BalanceEntity getBalance(UUID ownerId) {
        return balanceRepository.findByAccount_OwnerId(ownerId).orElseThrow(RuntimeException::new);
    }

    /**
     * Updates the balance for the specified account owner by adding the given amount.
     * <p>
     * This method retrieves the current balance for the account associated with the given owner ID.
     * It then adds the specified amount to the current balance, updates the balance entity with the new
     * amount and the current timestamp, and saves the updated balance entity back to the repository.
     * If no balance is found for the given owner ID, a {@link RuntimeException} is thrown.
     *
     * @param ownerId The UUID of the account owner whose balance is to be updated.
     *                This value cannot be null and should correspond to an existing account.
     * @param amount  The amount to be added to the current balance. This value can be positive or negative.
     *                If negative, it will decrease the current balance.
     * @return The updated {@link BalanceEntity} with the new balance amount and last update timestamp.
     * This entity reflects the changes made by this method.
     * @throws RuntimeException if no balance is found for the specified owner ID.
     *                          This indicates that the requested account does not exist in the repository.
     */
    public BalanceEntity updateBalance(UUID ownerId, BigDecimal amount) {
        Optional<BalanceEntity> currentBalance = balanceRepository.findByAccount_OwnerId(ownerId);
        BalanceEntity balanceEntity = currentBalance.orElseThrow(RuntimeException::new);

        BigDecimal updateAmount = balanceEntity.getAmount().add(amount);
        balanceEntity.setAmount(updateAmount);
        balanceEntity.setLastUpdate(Instant.now());

        return balanceRepository.save(balanceEntity);
    }

}
