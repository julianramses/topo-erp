package com.topo.topo_erp.dto;

import com.topo.topo_erp.model.Case.CaseState;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaseRequest {
    private String caseName;
    private String description;
    private Long personInChargeId; // ID of PersonInCharge
    private String location;
    private String coordinates;
    private CaseState state;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal budget;
}