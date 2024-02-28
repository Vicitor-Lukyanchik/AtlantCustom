package com.example.custom.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tour")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "season_id")
    private Season season;

    @Column(name = "number")
    @Min(value = 1, message = "Tour number can't be less than 1")
    @Max(value = 50, message = "Tour number can't be more than 50")
    private Integer number;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tour_match",
            joinColumns = {@JoinColumn(name = "tour_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "matches_id", referencedColumnName = "id")})
    private List<Match> matches;
}
