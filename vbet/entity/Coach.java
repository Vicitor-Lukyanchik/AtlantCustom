package com.example.custom.vbet.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "coach")
public class Coach {

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "coach")
    private List<TeamCoach> teamCoaches;
}
