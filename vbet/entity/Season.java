package com.example.custom.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "season")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_year")
    @Min(value = 1900, message = "Season start year can't be less than 1900")
    @Max(value = 2200, message = "Season start year can't be more than 2200")
    private Integer startYear;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "national_championship_id")
    private NationalChampionship championship;

    @OneToMany(mappedBy = "season")
    private List<TablePlace> tablePlaces;

    @OneToMany(mappedBy = "season")
    private List<Tour> tours;
}
