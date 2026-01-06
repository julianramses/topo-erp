package com.topo.topo_erp.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "persons_in_charge")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonInCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 8)
    private String rut; // 8 digits for now

    private String email;
    private String phoneNumber;
    private String position;
    private String department;

    @OneToMany(mappedBy = "personInCharge")
    @Builder.Default
    private List<Case> cases = new ArrayList<>();
}