package com.topo.topo_erp.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonInChargeRequest {
    private String name;
    private String rut; // 8 digits
    private String email;
    private String phoneNumber;
    private String position;
    private String department;
}