package com.quicktransfer.account.controller;

import com.quicktransfer.account.dto.BalanceDto;
import com.quicktransfer.account.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

import static com.quicktransfer.account.mapper.BalanceMapper.mapToDto;

@RestController
@RequestMapping("v1/account/balance")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @Operation(summary = "To retrieve the account balance by owner id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "account balance details fetched successfully", content =
                    {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BalanceDto.class))}),
            @ApiResponse(responseCode = "400", description = "invalid request", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server error, the details are logged on "
                    + "backend", content = @Content)})
    @GetMapping(value = "/{ownerId}", produces = "application/json")
    public ResponseEntity<BalanceDto> getBalance(@PathVariable("ownerId") UUID ownerId) {

        var balanceEntity = balanceService.getBalance(ownerId);
        return new ResponseEntity<>(mapToDto(balanceEntity), HttpStatus.OK);

    }

    @Operation(summary = "To add the balance to a particular account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "balance added successfully", content =
                    {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BalanceDto.class))}),
            @ApiResponse(responseCode = "400", description = "invalid request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server error, the details are logged on "
                    + "backend", content = @Content)})
    @PutMapping(value = "/{ownerId}", produces = "application/json")
    public ResponseEntity<BalanceDto> updateBalance(@PathVariable("ownerId") UUID ownerId, @RequestParam BigDecimal amount) {

        var balanceEntity = balanceService.updateBalance(ownerId, amount);
        return new ResponseEntity<>(mapToDto(balanceEntity), HttpStatus.OK);
    }


}
