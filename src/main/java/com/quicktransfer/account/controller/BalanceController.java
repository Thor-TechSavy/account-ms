package com.quicktransfer.account.controller;

import com.quicktransfer.account.dto.BalanceDto;
import com.quicktransfer.account.entity.BalanceEntity;
import com.quicktransfer.account.service.BalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("v1/account/balance")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<BalanceDto> getBalance(@PathVariable("ownerId") UUID ownerId) {
        BalanceEntity balance = balanceService.getBalance(ownerId);

        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setBalance(balance.getAmount());
        balanceDto.setOwnerId(balance.getAccount().getOwnerId());
        balanceDto.setCurrency(balance.getAccount().getCurrency());
        balanceDto.setLastUpdate(balance.getLastUpdate());

        return new ResponseEntity<>(balanceDto, HttpStatus.OK);

    }

    @PutMapping("/{ownerId}")
    public void updateBalance(@PathVariable("ownerId") UUID ownerId, @RequestParam BigDecimal amount) {
        balanceService.updateBalance(ownerId, amount);
    }


}
