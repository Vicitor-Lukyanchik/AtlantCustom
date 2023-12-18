package com.example.custom.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @Size(min = 6, max = 50, message = "Username should be more then 4 and less than 50")
    private String username;

    @Column(name = "password")
    @Size(min = 8, max = 60, message = "Password should be more then 8 and less than 60")
    private String password;

}
