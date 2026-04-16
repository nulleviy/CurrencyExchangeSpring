package com.currency_exchange.dto.exchange;

import com.currency_exchange.model.Currency;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExchangeRateRequest {

    @NotNull(message = "base currency is required")
    private String baseCurrencyCode;

    @NotNull(message = "target currency is required")
    private String targetCurrencyCode;

    @NotNull(message = "exchange rate is required")
    @Size(min = 1,max = 6,message = "exchange rate must be between 1 and 6 digits")
    private BigDecimal rate;

}
