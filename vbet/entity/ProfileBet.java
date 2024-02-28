package com.example.custom.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "profile_bet")
public class ProfileBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "match_bet_id")
    private MatchBet matchBet;

    @Column(name = "amount")
    @DecimalMin(value = "0.10", message = "Profile bet amount can't be less than 0.10")
    @Digits(integer=8, fraction=2, message = "Profile bet amount digits more than 8 and fraction more than 2")
    private BigDecimal amount;
}
