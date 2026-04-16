package com.currency_exchange.model;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "currencies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "sign", nullable = false)
    private String sign;

}
