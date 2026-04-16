package com.currency_exchange.model;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;

@Table(name = "exchange_rates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "base_currency_id", nullable = false)
    private Currency baseCurrency;

    @ManyToOne
    @JoinColumn(name = "target_currency_id", nullable = false)
    private Currency targetCurrency;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

}
