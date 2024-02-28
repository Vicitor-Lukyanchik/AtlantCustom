package com.example.custom.vbet.entity;

import com.example.custom.entity.Consumption;
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
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    @Size(min = 6, max = 60, message = "Login should be more then 6 and less than 60")
    private String login;

    @Column(name = "password")
    @Size(min = 6, max = 60, message = "Password should be more then 6 and less than 60")
    private String password;

    @Column(name = "firstname")
    @Size(min = 2, max = 150, message = "Firstname should be more than 2 and less than 150")
    @Pattern(regexp = "^[A-Z][a-z0-9_-]{3,99}$", message = "First letter in firstname should be uppercase")
    private String firstname;

    @Column(name = "lastname")
    @Size(min = 2, max = 150, message = "Lastname should be more than 2 and less than 150")
    @Pattern(regexp = "^[A-Z][a-z0-9_-]{3,99}$", message = "First letter in lastname should be uppercase")
    private String lastname;

    @Column(name = "email")
    @Size(max = 150, message = "Email should be less than 150")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "balance")
    @DecimalMin(value = "0.10", message = "Profile bet amount can't be less than 0.10")
    @Digits(integer=8, fraction=2, message = "Profile bet amount digits more than 8 and fraction more than 2")
    private BigDecimal balance;

    @Column(name = "dob")
    private LocalDate dob;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "profile_role",
            joinColumns = {@JoinColumn(name = "profile_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    @OneToMany(mappedBy = "profile")
    private List<BalanceTransaction> transactions;

    @OneToMany(mappedBy = "profile")
    private List<ProfileBet> profileBets;
}
