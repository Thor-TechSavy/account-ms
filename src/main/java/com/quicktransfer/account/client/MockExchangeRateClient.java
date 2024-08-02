package com.quicktransfer.account.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("local")
public class MockExchangeRateClient implements ExchangeRateClient {

    @Override
    public Optional<Double> getExchangeRate(String fromCurrency, String toCurrency) {
        return Optional.of(0.85);
    }
}
