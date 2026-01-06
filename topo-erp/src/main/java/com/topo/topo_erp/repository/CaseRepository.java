package com.topo.topo_erp.repository;

import com.topo.topo_erp.model.Case;
import com.topo.topo_erp.model.Case.CaseState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
    Optional<Case> findByCaseNumber(String caseNumber);
    List<Case> findByState(CaseState state);
    List<Case> findByPersonInChargeId(Long personInChargeId);
    List<Case> findByCaseNameContainingIgnoreCase(String name);
    List<Case> findByLocationContainingIgnoreCase(String location);
    boolean existsByCaseNumber(String caseNumber);
}