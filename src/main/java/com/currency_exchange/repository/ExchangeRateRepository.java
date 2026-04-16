package com.currency_exchange.repository;

import com.currency_exchange.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate,Long> {

    ExchangeRate findByBaseCurrencyIdAndTargetCurrencyId(Long baseId, Long targetId);

}
