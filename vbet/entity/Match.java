package com.example.custom.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(max = 155, message = "Match name should be less than 155")
    private String name;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "is_played")
    private Boolean isPlayed;

    @OneToMany(mappedBy = "match")
    private List<MatchBet> matchBets;

    @OneToMany(mappedBy = "match")
    private List<MatchTeam> matchTeams;

    @ManyToMany(mappedBy = "matches", fetch = FetchType.EAGER)
    private List<Tour> tours;
}
