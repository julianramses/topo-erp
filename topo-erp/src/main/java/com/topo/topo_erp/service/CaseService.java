package com.topo.topo_erp.service;

import com.topo.topo_erp.dto.CaseRequest;
import com.topo.topo_erp.dto.CaseResponse;
import com.topo.topo_erp.model.Case.CaseState;
import java.util.List;

public interface CaseService {
    CaseResponse createCase(CaseRequest request);
    CaseResponse getCaseById(Long id);
    CaseResponse getCaseByCaseNumber(String caseNumber);
    List<CaseResponse> getAllCases();
    List<CaseResponse> getCasesByState(CaseState state);
    List<CaseResponse> getCasesByPersonInCharge(Long personInChargeId);
    CaseResponse updateCase(Long id, CaseRequest request);
    CaseResponse updateCaseState(Long id, CaseState state);
    void deleteCase(Long id);
    List<CaseResponse> searchCases(String keyword); // Search by name or location
}