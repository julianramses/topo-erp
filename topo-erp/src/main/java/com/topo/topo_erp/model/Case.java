package com.topo.topo_erp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_rut")
    private String clientRut;

    @Column(nullable = false, unique = true)
    private String caseNumber; // Auto-generated format: CASE-2024-001

    @Column(nullable = false)
    private String caseName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_in_charge_id", nullable = false)
    private PersonInCharge personInCharge;

    @Column(nullable = false)
    private String location;

    private String coordinates; // GPS coordinates for mapping

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CaseState state;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal budget;
    private BigDecimal actualCost;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "caseEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CaseFile> files = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Auto-generate case number if not set
        if (caseNumber == null) {
            // You can implement your own numbering logic
            caseNumber = "CASE-" + LocalDate.now().getYear() + "-" + String.format("%03d", id != null ? id : 0);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum CaseState {
        OPEN,
        IN_PROGRESS,
        PENDING_REVIEW,
        CLOSED,
        ARCHIVED,
        CANCELLED
    }
}