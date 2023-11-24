package com.example.custom.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "declaration")
public class Declaration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_kod")
    private Long idCode;

    @Column(name = "is_import")
    private Boolean isImport;

    @Column(name = "declaration_date")
    private LocalDate date;

    @Column(name = "number")
    private String number;

    @Column(name = "import_application_number")
    private String importApplicationNumber;

    @Column(name = "contract_number")
    private String contractNumber;

    @Column(name = "contract_date")
    private LocalDate contractDate;

    @Column(name = "firm_name")
    private String firmName;
}
