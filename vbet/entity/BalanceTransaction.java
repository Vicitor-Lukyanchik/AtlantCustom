package com.example.custom.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "balance_transaction")
public class BalanceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    @DecimalMin(value = "0.10", message = "Profile bet amount can't be less than 0.10")
    @Digits(integer=8, fraction=2, message = "Profile bet amount digits more than 8 and fraction more than 2")
    private BigDecimal amount;

    @Column(name = "is_positive")
    private Boolean isPositive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
