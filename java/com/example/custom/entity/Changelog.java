package com.example.custom.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "changelog")
public class Changelog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "change_date_time")
    private LocalDateTime localDateTime;

    @Column(name = "all_count")
    private Integer allCount;

    @Column(name = "added_count")
    private Integer addedCount;
}
