package com.example.custom.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname")
    @Size(min = 2, max = 150, message = "Firstname should be more than 2 and less than 150")
    @Pattern(regexp = "^[A-Z][a-z0-9_-]{3,99}$", message = "First letter in firstname should be uppercase")
    private String firstname;

    @Column(name = "lastname")
    @Size(min = 2, max = 150, message = "Lastname should be more than 2 and less than 150")
    @Pattern(regexp = "^[A-Z][a-z0-9_-]{3,99}$", message = "First letter in lastname should be uppercase")
    private String lastname;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "height")
    @Min(value = 100, message = "Player height can't be less than 100")
    private Integer height;

    @Column(name = "number")
    @Min(value = 1, message = "Player height can't be less than 1")
    @Max(value = 999, message = "Player height can't be more than 999")
    private Integer number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "football_position_id")
    private FootballPosition position;

    @OneToMany(mappedBy = "player")
    private List<TeamPlayer> teamPlayers;

    @OneToMany(mappedBy = "player")
    private List<Goal> goals;
}
