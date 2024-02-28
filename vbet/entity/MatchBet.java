package com.example.custom.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "match_bet")
public class MatchBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matches_id")
    private Match match;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bet_id")
    private Bet bet;

    @Column(name = "coefficient")
    @Min(value = 1, message = "Coefficient cant be less than 1")
    private Double coefficient;
}
