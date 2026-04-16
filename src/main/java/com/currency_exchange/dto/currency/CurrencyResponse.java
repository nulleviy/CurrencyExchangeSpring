package com.currency_exchange.dto.currency;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CurrencyResponse {

    private Long id;
    private String code;
    private String fullName;
    private String sign;

}
