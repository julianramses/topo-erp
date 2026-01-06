package com.topo.topo_erp.service;

import com.topo.topo_erp.dto.CaseRequest;
import com.topo.topo_erp.dto.CaseResponse;
import com.topo.topo_erp.model.Case;
import com.topo.topo_erp.model.Case.CaseState;
import com.topo.topo_erp.model.PersonInCharge;
import com.topo.topo_erp.repository.CaseRepository;
import com.topo.topo_erp.repository.PersonInChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {

    private final CaseRepository caseRepository;
    private final PersonInChargeRepository personInChargeRepository;

    @Override
    public CaseResponse createCase(CaseRequest request) {
        // Validate required fields
        if (request.getCaseName() == null || request.getCaseName().trim().isEmpty()) {
            throw new IllegalArgumentException("Case name is required");
        }

        if (request.getPersonInChargeId() == null) {
            throw new IllegalArgumentException("Person in charge is required");
        }

        if (request.getLocation() == null || request.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Location is required");
        }

        // Get person in charge
        PersonInCharge personInCharge = personInChargeRepository.findById(request.getPersonInChargeId())
                .orElseThrow(() -> new RuntimeException("Person in charge not found with id: " + request.getPersonInChargeId()));

        // Create case
        Case newCase = Case.builder()
                .caseName(request.getCaseName())
                .description(request.getDescription())
                .personInCharge(personInCharge)
                .location(request.getLocation())
                .coordinates(request.getCoordinates())
                .state(request.getState() != null ? request.getState() : CaseState.OPEN)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .budget(request.getBudget())
                .build();

        Case savedCase = caseRepository.save(newCase);

        // Update case number with ID
        savedCase.setCaseNumber(generateCaseNumber(savedCase.getId()));
        savedCase = caseRepository.save(savedCase);

        return mapToResponse(savedCase);
    }

    @Override
    public CaseResponse getCaseById(Long id) {
        Case caseEntity = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found with id: " + id));
        return mapToResponse(caseEntity);
    }

    @Override
    public CaseResponse getCaseByCaseNumber(String caseNumber) {
        Case caseEntity = caseRepository.findByCaseNumber(caseNumber)
                .orElseThrow(() -> new RuntimeException("Case not found with number: " + caseNumber));
        return mapToResponse(caseEntity);
    }

    @Override
    public List<CaseResponse> getAllCases() {
        return caseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CaseResponse> getCasesByState(CaseState state) {
        return caseRepository.findByState(state)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CaseResponse> getCasesByPersonInCharge(Long personInChargeId) {
        return caseRepository.findByPersonInChargeId(personInChargeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CaseResponse updateCase(Long id, CaseRequest request) {
        Case caseEntity = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found with id: " + id));

        // Update fields if provided
        if (request.getCaseName() != null) {
            caseEntity.setCaseName(request.getCaseName());
        }

        if (request.getDescription() != null) {
            caseEntity.setDescription(request.getDescription());
        }

        if (request.getPersonInChargeId() != null) {
            PersonInCharge personInCharge = personInChargeRepository.findById(request.getPersonInChargeId())
                    .orElseThrow(() -> new RuntimeException("Person in charge not found with id: " + request.getPersonInChargeId()));
            caseEntity.setPersonInCharge(personInCharge);
        }

        if (request.getLocation() != null) {
            caseEntity.setLocation(request.getLocation());
        }

        if (request.getCoordinates() != null) {
            caseEntity.setCoordinates(request.getCoordinates());
        }

        if (request.getState() != null) {
            caseEntity.setState(request.getState());
        }

        if (request.getStartDate() != null) {
            caseEntity.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            caseEntity.setEndDate(request.getEndDate());
        }

        if (request.getBudget() != null) {
            caseEntity.setBudget(request.getBudget());
        }

        Case updatedCase = caseRepository.save(caseEntity);
        return mapToResponse(updatedCase);
    }

    @Override
    public CaseResponse updateCaseState(Long id, CaseState state) {
        Case caseEntity = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found with id: " + id));

        caseEntity.setState(state);
        Case updatedCase = caseRepository.save(caseEntity);
        return mapToResponse(updatedCase);
    }

    @Override
    public void deleteCase(Long id) {
        Case caseEntity = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found with id: " + id));

        // Check if case has files before deleting
        if (caseEntity.getFiles() != null && !caseEntity.getFiles().isEmpty()) {
            throw new RuntimeException("Cannot delete case with attached files. Delete files first.");
        }

        caseRepository.delete(caseEntity);
    }

    @Override
    public List<CaseResponse> searchCases(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCases();
        }

        // Search in case name and location
        List<Case> byName = caseRepository.findByCaseNameContainingIgnoreCase(keyword);
        List<Case> byLocation = caseRepository.findByLocationContainingIgnoreCase(keyword);

        // Combine and remove duplicates
        List<Case> allCases = new java.util.ArrayList<>(byName);
        for (Case c : byLocation) {
            if (!allCases.contains(c)) {
                allCases.add(c);
            }
        }

        return allCases.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private String generateCaseNumber(Long id) {
        int year = java.time.LocalDate.now().getYear();
        return String.format("CASE-%d-%03d", year, id);
    }

    private CaseResponse mapToResponse(Case caseEntity) {
        return CaseResponse.builder()
                .id(caseEntity.getId())
                .caseNumber(caseEntity.getCaseNumber())
                .caseName(caseEntity.getCaseName())
                .description(caseEntity.getDescription())
                .personInCharge(mapPersonInChargeToResponse(caseEntity.getPersonInCharge()))
                .location(caseEntity.getLocation())
                .coordinates(caseEntity.getCoordinates())
                .state(caseEntity.getState())
                .startDate(caseEntity.getStartDate())
                .endDate(caseEntity.getEndDate())
                .budget(caseEntity.getBudget())
                .actualCost(caseEntity.getActualCost())
                .createdAt(caseEntity.getCreatedAt())
                .updatedAt(caseEntity.getUpdatedAt())
                .fileCount(caseEntity.getFiles() != null ? caseEntity.getFiles().size() : 0)
                .build();
    }

    private com.topo.topo_erp.dto.PersonInChargeResponse mapPersonInChargeToResponse(PersonInCharge person) {
        if (person == null) {
            return null;
        }
        return com.topo.topo_erp.dto.PersonInChargeResponse.builder()
                .id(person.getId())
                .name(person.getName())
                .rut(person.getRut())
                .email(person.getEmail())
                .phoneNumber(person.getPhoneNumber())
                .position(person.getPosition())
                .department(person.getDepartment())
                .totalCases(person.getCases() != null ? person.getCases().size() : 0)
                .build();
    }
}