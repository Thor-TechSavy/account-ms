package com.quicktransfer.account.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(value = "currencyExchange", url = "${app.currencyExchange.url}")
@Profile("!local")
public interface ExchangeRateClient {

    @GetMapping
    Optional<Double> getExchangeRate(@RequestParam("fromCurrency") String fromCurrency, @RequestParam("toCurrency") String toCurrency);

}
