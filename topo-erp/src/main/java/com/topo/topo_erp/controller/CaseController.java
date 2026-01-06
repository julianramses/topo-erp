package com.topo.topo_erp.controller;

import com.topo.topo_erp.dto.CaseRequest;
import com.topo.topo_erp.dto.CaseResponse;
import com.topo.topo_erp.model.Case.CaseState;
import com.topo.topo_erp.service.CaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
public class CaseController {

    private final CaseService caseService;

    // Create a new case
    @PostMapping
    public ResponseEntity<CaseResponse> createCase(@Valid @RequestBody CaseRequest caseRequest) {
        CaseResponse createdCase = caseService.createCase(caseRequest);
        return new ResponseEntity<>(createdCase, HttpStatus.CREATED);
    }

    // Get case by ID
    @GetMapping("/{id}")
    public ResponseEntity<CaseResponse> getCaseById(@PathVariable Long id) {
        CaseResponse caseResponse = caseService.getCaseById(id);
        return ResponseEntity.ok(caseResponse);
    }

    // Get case by case number
    @GetMapping("/number/{caseNumber}")
    public ResponseEntity<CaseResponse> getCaseByCaseNumber(@PathVariable String caseNumber) {
        CaseResponse caseResponse = caseService.getCaseByCaseNumber(caseNumber);
        return ResponseEntity.ok(caseResponse);
    }

    // Get all cases
    @GetMapping
    public ResponseEntity<List<CaseResponse>> getAllCases() {
        List<CaseResponse> cases = caseService.getAllCases();
        return ResponseEntity.ok(cases);
    }

    // Get cases by state
    @GetMapping("/state/{state}")
    public ResponseEntity<List<CaseResponse>> getCasesByState(@PathVariable CaseState state) {
        List<CaseResponse> cases = caseService.getCasesByState(state);
        return ResponseEntity.ok(cases);
    }

    // Get cases by person in charge
    @GetMapping("/person-in-charge/{personId}")
    public ResponseEntity<List<CaseResponse>> getCasesByPersonInCharge(@PathVariable Long personId) {
        List<CaseResponse> cases = caseService.getCasesByPersonInCharge(personId);
        return ResponseEntity.ok(cases);
    }

    // Update case
    @PutMapping("/{id}")
    public ResponseEntity<CaseResponse> updateCase(
            @PathVariable Long id,
            @Valid @RequestBody CaseRequest caseRequest) {
        CaseResponse updatedCase = caseService.updateCase(id, caseRequest);
        return ResponseEntity.ok(updatedCase);
    }

    // Update only case state
    @PatchMapping("/{id}/state")
    public ResponseEntity<CaseResponse> updateCaseState(
            @PathVariable Long id,
            @RequestParam CaseState state) {
        CaseResponse updatedCase = caseService.updateCaseState(id, state);
        return ResponseEntity.ok(updatedCase);
    }

    // Delete case
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        caseService.deleteCase(id);
        return ResponseEntity.noContent().build();
    }

    // Search cases
    @GetMapping("/search")
    public ResponseEntity<List<CaseResponse>> searchCases(@RequestParam String keyword) {
        List<CaseResponse> cases = caseService.searchCases(keyword);
        return ResponseEntity.ok(cases);
    }

    // Get case statistics (optional)
    @GetMapping("/statistics")
    public ResponseEntity<CaseStatistics> getCaseStatistics() {
        List<CaseResponse> allCases = caseService.getAllCases();

        long openCases = allCases.stream()
                .filter(c -> c.getState() == CaseState.OPEN)
                .count();

        long inProgressCases = allCases.stream()
                .filter(c -> c.getState() == CaseState.IN_PROGRESS)
                .count();

        long closedCases = allCases.stream()
                .filter(c -> c.getState() == CaseState.CLOSED)
                .count();

        CaseStatistics stats = new CaseStatistics(
                allCases.size(),
                openCases,
                inProgressCases,
                closedCases
        );

        return ResponseEntity.ok(stats);
    }

    // Inner class for statistics
    public record CaseStatistics(
            long totalCases,
            long openCases,
            long inProgressCases,
            long closedCases
    ) {}
}