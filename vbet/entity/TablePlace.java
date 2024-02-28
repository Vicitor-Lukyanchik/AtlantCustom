package com.example.custom.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "table_place")
public class TablePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "number")
    @Min(value = 1, message = "Place number can't be less than 1")
    @Max(value = 50, message = "Place number can't be more than 50")
    private Integer number;

    @Column(name = "match_count")
    @Min(value = 0, message = "Match count can't be less than 0")
    @Max(value = 50, message = "Match count can't be more than 50")
    private Integer match_count;

    @Column(name = "points")
    @Min(value = 0, message = "Points can't be less than 0")
    @Max(value = 200, message = "Points can't be more than 200")
    private Integer points;

    @Column(name = "difference")
    @Min(value = -300, message = "Difference can't be less than -300")
    @Max(value = 300, message = "Difference can't be more than 300")
    private Integer difference;

    @Column(name = "scored")
    @Min(value = -300, message = "Scored can't be less than -300")
    @Max(value = 300, message = "Scored can't be more than 300")
    private Integer scored;
}
