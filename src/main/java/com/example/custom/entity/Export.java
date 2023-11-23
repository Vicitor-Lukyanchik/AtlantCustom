package com.example.custom.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "export")
public class Export {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "declaration_id")
    private Declaration declaration;

    @Column(name = "tnved")
    private String tnved;

    @Column(name = "ttn_series")
    private String ttnSeries;

    @Column(name = "ttn_number")
    private String ttnNumber;

    @Column(name = "ttn_date")
    private LocalDate ttnDate;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_weight")
    private Double productWeight;

    @Column(name = "product_count")
    private Integer productCount;

    @Column(name = "product_sum")
    private BigDecimal productSum;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "fio")
    private String fio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id")
    private Unit unit;
}
