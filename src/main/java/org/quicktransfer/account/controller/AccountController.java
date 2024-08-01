package org.quicktransfer.account.controller;

import org.quicktransfer.account.dto.AccountDetailsDto;
import org.quicktransfer.account.dto.CreateAccountDto;
import org.quicktransfer.account.dto.UpdateAccountDto;
import org.quicktransfer.account.entity.AccountEntity;
import org.quicktransfer.account.exceptions.InvalidRequestException;
import org.quicktransfer.account.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDetailsDto> createAccount(@RequestBody final CreateAccountDto accountDto) {
        validateAccountCreationRequest(accountDto);
        AccountEntity accountEntity = accountService.createAccount(accountDto);

        AccountDetailsDto accountDetailsDto = new AccountDetailsDto();
        accountDetailsDto.setOwnerId(accountEntity.getOwnerId());
        accountDetailsDto.setBalance(accountEntity.getBalance());
        accountDetailsDto.setCurrency(accountEntity.getCurrency());
        accountDetailsDto.setFirstName(accountEntity.getFirstName());
        accountDetailsDto.setLastName(accountEntity.getLastName());
        accountDetailsDto.setDob(accountEntity.getDob());

        return new ResponseEntity<>(accountDetailsDto, HttpStatus.CREATED);

    }

    @PutMapping("/{ownerId}")
    public void updateAccount(@PathVariable UUID ownerId, @RequestBody final UpdateAccountDto accountDto) {

        accountService.updateAccount(ownerId, accountDto);

    }

    private void validateAccountCreationRequest(final CreateAccountDto accountDto) {
        boolean isFirstNameNullOrBlank = accountDto.getFirstName() == null || accountDto.getFirstName().isEmpty();
        boolean isLastNameNullOrBlank = accountDto.getLastName() == null || accountDto.getLastName().isEmpty();
        boolean isCurrencyNullOrBlank = accountDto.getCurrency() == null || accountDto.getCurrency().isEmpty();

        if (isFirstNameNullOrBlank || isLastNameNullOrBlank || isCurrencyNullOrBlank) {
            throw new InvalidRequestException("Either firstName or lastName or currency or owner id is null");
        }
    }
}
