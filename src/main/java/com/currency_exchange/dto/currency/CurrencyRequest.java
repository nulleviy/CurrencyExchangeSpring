package com.currency_exchange.dto.currency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CurrencyRequest {

    @NotBlank(message = "Currency code is required")
    @Size(min = 3, max = 3,message = "Currency code length can only be 3 chars in length")
    private String code;

    @NotBlank(message = "Currency name is required")
    @Size(min = 2,max = 64,message = "Currency name must be between 2 and 64 chars")
    private String fullName;

    @NotBlank(message = "Currency sign is required")
    @Size(min = 1,max = 10,message = "Currency sign must be between 1 and 10 chars")
    private String sign;

}
