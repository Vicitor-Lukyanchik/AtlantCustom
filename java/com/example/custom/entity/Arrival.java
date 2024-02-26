package com.example.custom.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "arrival")
public class Arrival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "declaration_id")
    private Declaration declaration;

    @Column(name = "storehouse_id")
    private Long storehouseId;


    @Column(name = "storehouse_number")
    private String storehouseNumber;


    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "turnover_count")
    private Double turnoverCount;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id")
    private Currency currency;


    @Column(name = "external_receiver_code")
    private Integer externalReceiverCode;

    @Column(name = "external_receiver_name")
    private String externalReceiverName;


    @Column(name = "document_type")
    private Integer documentType;

    @Column(name = "document_number")
    private Integer documentNumber;

    @Column(name = "reporting_month")
    private Integer reportingMonth;

    @Column(name = "reporting_year")
    private Integer reportingYear;


    @Column(name = "accompanying_document_number")
    private String accompanyingDocumentNumber;

    @Column(name = "accompanying_document_series")
    private String accompanyingDocumentSeries;

    @Column(name = "accompanying_document_date")
    private LocalDate accompanyingDocumentDate;


    @Column(name = "nom_f")
    private String nom_f;

    @Column(name = "dat_f")
    private LocalDate dat_f;


    @Column(name = "workshop_receiver")
    private String workshopReceiver;

    @Column(name = "application_number")
    private String applicationNumber;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "arrival")
    private List<Consumption> consumptions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arrival arrival = (Arrival) o;
        return Objects.equals(storehouseNumber, arrival.storehouseNumber) && Objects.equals(productName, arrival.productName) && Objects.equals(turnoverCount, arrival.turnoverCount) && Objects.equals(productPrice, arrival.productPrice) && isConsumptionsEquals(arrival.consumptions);
    }

    private boolean isConsumptionsEquals(List<Consumption> consumptions) {
        if (consumptions.size() != this.getConsumptions().size()) {
            return false;
        }
        for (int i = 0; i < consumptions.size(); i++) {
            if(!consumptions.get(i).equals(this.consumptions.get(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storehouseNumber, productName, turnoverCount, productPrice);
    }
}
