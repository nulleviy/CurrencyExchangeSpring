package com.currency_exchange.service;

import com.currency_exchange.dto.currency.CompositeCurrency;
import com.currency_exchange.dto.currency.CurrencyRequest;
import com.currency_exchange.model.Currency;
import com.currency_exchange.repository.CurrencyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<CompositeCurrency> getCurrencies() {

        List<Currency> currencies = currencyRepository.findAll();

        log.info("Пользователь вывел список валют: {}",currencies);
        return ResponseEntity.status(HttpStatus.OK).body(CompositeCurrency.success(currencies));

    }

    @Transactional(readOnly = true)
    public ResponseEntity<CompositeCurrency> getCurrency(String code) {

        if(code == null || code.isBlank()){

            log.info("Ошибка с запросом на получение валюты невалидный код валюты: {}",code);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CompositeCurrency.badRequest());
        }

        Currency currency = currencyRepository.findByCode(code);

        if(currency == null){

            log.info("Не найдена валюта с таким кодом: {}", code);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeCurrency.notFound());
        }

        log.info("Пользователь вывел валюту: {}", currency);
        return ResponseEntity.status(HttpStatus.OK).body(CompositeCurrency.success(currency));
    }

    @Transactional(readOnly = false)
    public ResponseEntity<CompositeCurrency> addCurrency(CurrencyRequest request) {

        if(request == null){

            log.info("Запрос на добавление валюты пустой");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CompositeCurrency.badRequest());
        }

        if(currencyRepository.findByCode(request.getCode())!=null){

            log.info("Валюта с таким кодом уже существует: {}",request.getCode());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CompositeCurrency.conflict());
        }

        Currency newCurrency = Currency.builder()
                .code(request.getCode())
                .fullName(request.getFullName())
                .sign(request.getSign())
                .build();

        currencyRepository.saveAndFlush(newCurrency);
        log.info("Добавлена новая валюта: {}",newCurrency);

        return  ResponseEntity.status(HttpStatus.OK).body(CompositeCurrency.success(newCurrency));

    }

}
