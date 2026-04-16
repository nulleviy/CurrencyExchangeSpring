package com.currency_exchange.dto.exchange;

import com.currency_exchange.model.ExchangeRate;
import com.currency_exchange.mapper.ExchangeMapper;
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
public class CompositeExchangeRate {

    private List<ExchangeRateResponse> exchangeRateResponses;
    private String message;
    private Instant date;

    public CompositeExchangeRate(String message){
        this.message = message;
    }

    public static CompositeExchangeRate badRequest(){
        return error("У тебя проблема с запросом");
    }

    public static CompositeExchangeRate notFound(){
        return error("Не найдено ничего по такому запросу");
    }

    public static CompositeExchangeRate conflict(){
        return error("Такая сущность уже существует");
    }

    public static CompositeExchangeRate success(List<ExchangeRate> exchangeRates) {

        return CompositeExchangeRate.builder()
                .exchangeRateResponses(ExchangeMapper.exchangeRateToResponseList(exchangeRates))
                .date(Instant.now())
                .build();
    }

    public static CompositeExchangeRate success(ExchangeRate exchangeRate){

        return CompositeExchangeRate.builder()
                .exchangeRateResponses(Collections.singletonList(ExchangeMapper.exchangeRateToResponse(exchangeRate)))
                .date(Instant.now())
                .build();
    }

    public static CompositeExchangeRate error(String message){

        return CompositeExchangeRate.builder()
                .message(message)
                .date(Instant.now())
                .build();
    }

}
