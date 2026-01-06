package com.topo.topo_erp.repository;

import com.topo.topo_erp.model.CaseFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CaseFileRepository extends JpaRepository<CaseFile, Long> {
    List<CaseFile> findByCaseEntityId(Long caseId);
    List<CaseFile> findByFileType(String fileType);
    void deleteByCaseEntityId(Long caseId);
}