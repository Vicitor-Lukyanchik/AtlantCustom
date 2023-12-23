package com.example.custom.dto;

import com.example.custom.entity.Declaration;
import com.example.custom.entity.Unit;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ExportDto {

    private String tnved;

    private String ttnSeries;

    private String ttnNumber;

    private String ttnDate;

    private String productName;

    private Double productWeight;

    private Integer productCount;

    private BigDecimal productSum;

    private String dateTime;

    private String fio;

    private String unit;

    private String message;
}