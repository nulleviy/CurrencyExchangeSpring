package com.currency_exchange.dto.currency;

import com.currency_exchange.mapper.CurrencyMapper;
import com.currency_exchange.model.Currency;

import lombok.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
public class CompositeCurrency {

    private List<CurrencyResponse> currencyResponses;
    private String message;
    private Instant date;

    public static CompositeCurrency badRequest(){
        return error("У тебя проблема с запросом");
    }

    public static CompositeCurrency notFound() {
        return error("Не найдено ничего по такому запросу");
    }

    public static CompositeCurrency conflict() {
        return error("Такая сущность уже существует");
    }

    public static CompositeCurrency success(List<Currency> currencyResponse){

        return CompositeCurrency.builder()
                .currencyResponses(CurrencyMapper.currencyToResponseList(currencyResponse))
                .date(Instant.now())
                .build();
    }

    public static CompositeCurrency success(Currency currencyResponse){

        return CompositeCurrency.builder()
                .currencyResponses(Collections.singletonList
                        (CurrencyMapper.currencyToResponse(currencyResponse)))
                .date(Instant.now())
                .build();
    }

    public static CompositeCurrency error(String message){

        return CompositeCurrency.builder()
                .message(message)
                .date(Instant.now())
                .build();

    }

}
