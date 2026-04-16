package com.currency_exchange.controller;

import com.currency_exchange.dto.currency.CompositeCurrency;
import com.currency_exchange.dto.currency.CurrencyRequest;
import com.currency_exchange.service.CurrencyService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/currencies")
    public ResponseEntity<CompositeCurrency> getCurrencies(){
        return currencyService.getCurrencies();
    }

    @GetMapping("/currency/{code}")
    public ResponseEntity<CompositeCurrency> getCurrency(@PathVariable String code){
        return currencyService.getCurrency(code);
    }

    @PostMapping("/currencies")
    public ResponseEntity<CompositeCurrency> addCurrency(@RequestBody CurrencyRequest request){
        return currencyService.addCurrency(request);
    }

}
