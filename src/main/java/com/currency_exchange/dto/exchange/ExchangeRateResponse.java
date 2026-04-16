package com.currency_exchange.dto.exchange;

import com.currency_exchange.model.Currency;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExchangeRateResponse {

    private Long id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;

}
