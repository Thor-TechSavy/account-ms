package com.quicktransfer.account.controller;

import com.quicktransfer.account.dto.AccountDetailsDto;
import com.quicktransfer.account.dto.CreateAccountDto;
import com.quicktransfer.account.entity.AccountEntity;
import com.quicktransfer.account.exceptions.InvalidRequestException;
import com.quicktransfer.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.quicktransfer.account.mapper.AccountMapper.mapToDto;
import static com.quicktransfer.account.mapper.AccountMapper.mapToEntity;

@RestController
@RequestMapping("/v1/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "To create the account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "account created", content =
                    {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDetailsDto.class))}),
            @ApiResponse(responseCode = "400", description = "invalid request payload", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server error, the details are logged on "
                    + "backend", content = @Content)})
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<AccountDetailsDto> createAccount(@RequestBody final CreateAccountDto accountDto) {
        validateAccountCreationRequest(accountDto);

        AccountEntity accountEntity = accountService.createAccount(mapToEntity(accountDto));

        AccountDetailsDto accountDetailsDto = mapToDto(accountEntity);

        return new ResponseEntity<>(accountDetailsDto, HttpStatus.CREATED);

    }

    @Operation(summary = "To retrieve the account details by owner id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "account fetched successfully", content =
                    {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDetailsDto.class))}),
            @ApiResponse(responseCode = "400", description = "invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server error, the details are logged on "
                    + "backend", content = @Content)})
    @GetMapping(value = "/{ownerId}", produces = "application/json")
    public ResponseEntity<AccountDetailsDto> getAccount(@PathVariable UUID ownerId) {

        AccountEntity account = accountService.getAccount(ownerId);

        AccountDetailsDto accountDetailsDto = mapToDto(account);

        return new ResponseEntity<>(accountDetailsDto, HttpStatus.OK);
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
