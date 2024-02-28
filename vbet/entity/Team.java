package com.example.custom.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Size(max = 99, message = "Team name should be less than 99")
    private String name;

    @Column(name = "stadium_name")
    @Size(max = 99, message = "Stadium name should be less than 99")
    private String stadiumName;

    @Column(name = "emblem_path")
    @Size(max = 250, message = "Emblem path should be less than 250")
    private String emblemPath;

    @OneToMany(mappedBy = "team")
    private List<TeamPlayer> teamPlayers;

    @OneToMany(mappedBy = "team")
    private List<TeamCoach> teamCoaches;

    @OneToMany(mappedBy = "team")
    private List<MatchTeam> matchTeams;

    @OneToMany(mappedBy = "team")
    private List<TablePlace> tablePlaces;
}
