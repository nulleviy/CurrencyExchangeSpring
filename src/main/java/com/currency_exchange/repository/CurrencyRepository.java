package com.currency_exchange.repository;

import com.currency_exchange.model.Currency;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByCode(String code);

}
