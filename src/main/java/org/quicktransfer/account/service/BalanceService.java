package org.quicktransfer.account.service;

import org.quicktransfer.account.entity.BalanceEntity;
import org.quicktransfer.account.repository.BalanceRepository;
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

    public void updateBalance(UUID ownerId, BigDecimal amount) {
        Optional<BalanceEntity> currentBalance = balanceRepository.findByAccount_OwnerId(ownerId);
        BalanceEntity balanceEntity = currentBalance.orElseThrow(RuntimeException::new);

        BigDecimal updateAmount = balanceEntity.getAmount().add(amount);
        balanceEntity.setAmount(updateAmount);
        balanceEntity.setLastUpdate(Instant.now());

        balanceRepository.save(balanceEntity);
    }

    public BalanceEntity getBalance(UUID ownerId) {
        return balanceRepository.findByAccount_OwnerId(ownerId).orElseThrow(RuntimeException::new);
    }
}
