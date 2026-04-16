package com.currency_exchange.service;

import com.currency_exchange.dto.exchange.CompositeExchangeRate;
import com.currency_exchange.dto.exchange.ExchangeDto;
import com.currency_exchange.dto.exchange.ExchangeRateRequest;
import com.currency_exchange.model.Currency;
import com.currency_exchange.model.ExchangeRate;
import com.currency_exchange.repository.CurrencyRepository;
import com.currency_exchange.repository.ExchangeRateRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<CompositeExchangeRate> getExchangeRates() {

        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();

        log.info("Пользователь вывел все курсы обмена: {}",exchangeRates);
        return ResponseEntity.status(HttpStatus.OK).body(CompositeExchangeRate.success(exchangeRates));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<CompositeExchangeRate> getExchangeRate(String exchange) {

        ExchangeRate exchangeRate = exchangeRateFromString(exchange).getBody();

        if(exchangeRate == null){
            log.info("Не найдено валют с таким");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeExchangeRate.notFound());
        }

        return ResponseEntity.status(HttpStatus.OK).body(CompositeExchangeRate.success(exchangeRate));
    }

    @Transactional(readOnly = false)
    public ResponseEntity<CompositeExchangeRate> addExchangeRate(ExchangeRateRequest request) {

        if(request == null){

            log.info("Запрос на добавление обменной пары валют пустой");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CompositeExchangeRate.badRequest());
        }

        Currency baseCurrency = currencyRepository.findByCode(request.getBaseCurrencyCode());
        Currency targetCurrency = currencyRepository.findByCode(request.getTargetCurrencyCode());

        Long baseId = baseCurrency.getId();
        Long targetId = targetCurrency.getId();

        if(exchangeRateRepository.findByBaseCurrencyIdAndTargetCurrencyId(baseId,targetId)!=null){

            log.info("Такой курс обмена валют уже существует: {}",request);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CompositeExchangeRate.conflict());
        }

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(request.getRate())
                .build();

        exchangeRateRepository.saveAndFlush(exchangeRate);
        log.info("Добавлен новый курс обмена валют: {}", exchangeRate);

        return ResponseEntity.status(HttpStatus.OK).body(CompositeExchangeRate.success(exchangeRate));
    }

    @Transactional(readOnly = false)
    public ResponseEntity<CompositeExchangeRate> updateExchangeRate(BigDecimal rate, String exchange) {

        if(rate == null || exchange == null || exchange.isBlank()){

            log.info("При обновлении курса обмена валют пользователь неправильно ввел запрос: rate {} exchange {}",rate,exchange);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CompositeExchangeRate.badRequest());
        }

        ExchangeRate exchangeRate = exchangeRateFromString(exchange).getBody();

        if(exchangeRate == null){

            log.info("Не найден курс обмена валют");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CompositeExchangeRate.notFound());
        }

        BigDecimal oldRate = exchangeRate.getRate();
        exchangeRate.setRate(rate);
        BigDecimal newRate = exchangeRate.getRate();

        exchangeRateRepository.saveAndFlush(exchangeRate);
        log.info("Курс обмена валют успешно сохранен: старый {} обновленный {}",oldRate,newRate);

        return ResponseEntity.status(HttpStatus.OK).body(CompositeExchangeRate.success(exchangeRate));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ExchangeDto> getExchange(String baseCode, String targetCode, BigDecimal amount) {

        String exchange = baseCode+targetCode;
        ExchangeRate exchangeRate = exchangeRateFromString(exchange).getBody();

        if(exchangeRate!=null) {

            ExchangeDto exchangeDto = ExchangeDto.builder()
                    .baseCurrency(exchangeRate.getBaseCurrency())
                    .targetCurrency(exchangeRate.getTargetCurrency())
                    .rate(exchangeRate.getRate())
                    .amount(amount)
                    .convertedAmount(amount.multiply(exchangeRate.getRate()))
                    .build();

            log.info("Пользователь использовал прямой обмен валют: из {} в {} в количестве {}", baseCode, targetCode, amount);
            return ResponseEntity.status(HttpStatus.OK).body(exchangeDto);
        }

        Currency baseCurrency = currencyRepository.findByCode(baseCode);
        Currency targetCurrency = currencyRepository.findByCode(targetCode);
        ExchangeRate reverseExchangeRate = exchangeRateRepository.findByBaseCurrencyIdAndTargetCurrencyId
                                                                  (targetCurrency.getId(),baseCurrency.getId());

        if(reverseExchangeRate != null){

            Currency baseCurrencyReverse = reverseExchangeRate.getTargetCurrency();
            Currency targetCurrencyReverse = reverseExchangeRate.getBaseCurrency();
            BigDecimal rate = reverseExchangeRate.getRate();

            ExchangeDto exchangeDto = ExchangeDto.builder()
                    .baseCurrency(baseCurrencyReverse)
                    .targetCurrency(targetCurrencyReverse)
                    .rate(rate).amount(amount)
                    .convertedAmount(amount.divide(rate, 3, RoundingMode.CEILING))
                    .build();

            log.info("Пользователь использовал обратный обмен валют: из {} в {} в количестве {}", baseCode, targetCode, amount);
            return ResponseEntity.status(HttpStatus.OK).body(exchangeDto);
        }

        ExchangeRate firstUsdExchangeRate = exchangeRateFromString("USD" + baseCurrency.getCode()).getBody();

        if(firstUsdExchangeRate == null){

            log.info("Не нашлось обмена из доллара в {}",baseCurrency.getCode());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ExchangeRate secondUsdExchangeRate = exchangeRateFromString("USD" + targetCurrency.getCode()).getBody();

        if(secondUsdExchangeRate == null){

            log.info("Не нашлось обмена из доллара в {}",targetCurrency.getCode());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        BigDecimal firstRate = firstUsdExchangeRate.getRate();
        BigDecimal secondRate = secondUsdExchangeRate.getRate();
        BigDecimal rate = firstRate.divide(secondRate,2,RoundingMode.CEILING);


        ExchangeDto exchangeDto = ExchangeDto.builder()
                                  .baseCurrency(baseCurrency)
                                  .targetCurrency(targetCurrency)
                                  .rate(rate).amount(amount)
                                  .convertedAmount(rate.multiply(amount))
                                  .build();

        log.info("Пользователь использовал реципрокный обмен валют: из {} в {} в количестве {}", baseCode, targetCode, amount);
        return ResponseEntity.status(HttpStatus.OK).body(exchangeDto);
    }

    private ResponseEntity<ExchangeRate> exchangeRateFromString(String exchange){

        if(exchange.length()!=6){

            log.info("Не валидная строчка валют: {}",exchange);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String firstCurrencyCode = exchange.substring(0,3);
        String secondCurrencyCode = exchange.substring(3,6);
        Currency firstCurrency = currencyRepository.findByCode(firstCurrencyCode);

        if(firstCurrency == null){

            log.info("Не найдено кода такой валюты: {}",firstCurrencyCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Currency secondCurrency = currencyRepository.findByCode(secondCurrencyCode);

        if(secondCurrency == null){

            log.info("Не найдено кода такой валюты: {}",secondCurrencyCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ExchangeRate exchangeRate = exchangeRateRepository.findByBaseCurrencyIdAndTargetCurrencyId
                                                            (firstCurrency.getId(),secondCurrency.getId());

        log.info("Успешно выведена валюта: {}",exchangeRate);
        return ResponseEntity.status(HttpStatus.OK).body(exchangeRate);
    }

}
