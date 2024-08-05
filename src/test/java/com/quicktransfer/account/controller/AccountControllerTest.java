package com.quicktransfer.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicktransfer.account.dto.AccountDetailsDto;
import com.quicktransfer.account.dto.CreateAccountDto;
import com.quicktransfer.account.entity.AccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static com.quicktransfer.account.mapper.AccountMapper.mapToDto;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateAccountDto createAccountDto;
    private AccountEntity accountEntity;
    private AccountDetailsDto accountDetailsDto;
    private UUID ownerId;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();

        createAccountDto = new CreateAccountDto();
        createAccountDto.setFirstName("John");
        createAccountDto.setLastName("Doe");
        createAccountDto.setCurrency("USD");
        createAccountDto.setDob("10-10-2000");

        accountEntity = new AccountEntity();
        accountEntity.setFirstName("John");
        accountEntity.setLastName("Doe");
        accountEntity.setCurrency("USD");
        accountEntity.setDob("10-10-2000");

        accountDetailsDto = mapToDto(accountEntity);
    }


    @Test
    void testCreateAccount() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAccountDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

}
