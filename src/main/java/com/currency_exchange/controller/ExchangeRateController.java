package com.currency_exchange.controller;

import com.currency_exchange.dto.exchange.CompositeExchangeRate;
import com.currency_exchange.dto.exchange.ExchangeDto;
import com.currency_exchange.dto.exchange.ExchangeRateRequest;
import com.currency_exchange.service.ExchangeRateService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/exchangeRates")
    public ResponseEntity<CompositeExchangeRate> getExchangeRates(){
        return exchangeRateService.getExchangeRates();
    }

    @GetMapping("/exchangeRate/{exchange}")
    public ResponseEntity<CompositeExchangeRate> getExchangeRate(@PathVariable String exchange){
        return exchangeRateService.getExchangeRate(exchange);
    }

    @PostMapping("/exchangeRates")
    public ResponseEntity<CompositeExchangeRate> addExchangeRate(@RequestBody ExchangeRateRequest request){
        return exchangeRateService.addExchangeRate(request);
    }

    @PatchMapping("/exchangeRate/{exchange}")
    public ResponseEntity<CompositeExchangeRate> updateExchangeRate(@PathVariable String exchange,
                                                                    @RequestBody BigDecimal rate){
        return exchangeRateService.updateExchangeRate(rate, exchange);
    }

    @GetMapping("/exchange")
    public ResponseEntity<ExchangeDto> getExchange(@RequestParam("from") String baseCode,
                                                   @RequestParam("to") String targetCode,
                                                   @RequestParam("amount")  BigDecimal amount){
        return exchangeRateService.getExchange(baseCode, targetCode, amount);
    }

}
