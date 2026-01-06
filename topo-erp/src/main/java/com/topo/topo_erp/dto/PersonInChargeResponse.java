package com.topo.topo_erp.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonInChargeResponse {
    private Long id;
    private String name;
    private String rut;
    private String email;
    private String phoneNumber;
    private String position;
    private String department;
    private int totalCases;
}