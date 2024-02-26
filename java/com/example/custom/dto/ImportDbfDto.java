package com.example.custom.dto;

import com.example.custom.entity.Currency;
import com.example.custom.entity.Unit;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ImportDbfDto {

    private Long id;

    private String nom_sklad;

    private LocalDate datao;

    private Integer typ;

    private String nomno;

    private String name;

    private Double kolobor;

    private Unit unit;

    private BigDecimal cenao;

    private Currency currency;

    private Integer shpolo;

    private String nam_sh;

    private Integer doco;

    private Integer nam_meso;

    private Integer god;

    private String doc_tr;

    private String ser_tr;

    private LocalDate dat_tr;

    private String nom_f;

    private LocalDate dat_f;

    private String d_nd;

    private LocalDate d_dath;

    private Integer d_kd;

    private String pol_ceh;

    private String d_tam_doc;
}
