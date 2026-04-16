package com.currency_exchange.mapper;

import com.currency_exchange.dto.exchange.ExchangeRateResponse;
import com.currency_exchange.model.ExchangeRate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeMapper {

    public static List<ExchangeRateResponse> exchangeRateToResponseList(List<ExchangeRate> exchangeRates){

        return exchangeRates.stream()
                            .map(ExchangeMapper::exchangeRateToResponse)
                            .toList();
    }

    public static ExchangeRateResponse exchangeRateToResponse(ExchangeRate exchangeRate) {

        return ExchangeRateResponse.builder()
                                   .id(exchangeRate.getId())
                                   .baseCurrency(exchangeRate.getBaseCurrency())
                                   .targetCurrency(exchangeRate.getTargetCurrency())
                                   .rate(exchangeRate.getRate())
                                   .build();
    }

}
