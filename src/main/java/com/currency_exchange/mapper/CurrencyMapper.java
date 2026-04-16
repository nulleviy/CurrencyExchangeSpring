package com.currency_exchange.mapper;

import com.currency_exchange.dto.currency.CurrencyResponse;
import com.currency_exchange.model.Currency;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrencyMapper {

    public static List<CurrencyResponse> currencyToResponseList(List<Currency> currencies){

        return currencies.stream()
                         .map(CurrencyMapper::currencyToResponse)
                         .toList();

    }

    public static CurrencyResponse currencyToResponse(Currency currency){

        return CurrencyResponse.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .fullName(currency.getFullName())
                .sign(currency.getSign())
                .build();
    }

}
