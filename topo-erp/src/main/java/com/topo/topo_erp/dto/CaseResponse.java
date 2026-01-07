package com.topo.topo_erp.dto;

import com.topo.topo_erp.model.Case.CaseState;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseResponse {
    private Long id;
    private String caseNumber;
    private String caseName;
    private String description;
    private PersonInChargeResponse personInCharge;
    private String location;
    private String coordinates;
    private CaseState state;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
    private BigDecimal actualCost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int fileCount;
    private String clientRut;
}