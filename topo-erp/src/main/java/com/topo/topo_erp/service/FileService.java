package com.topo.topo_erp.service;

import com.topo.topo_erp.dto.FileUploadRequest;
import com.topo.topo_erp.model.CaseFile;
import java.util.List;

public interface FileService {
    CaseFile uploadFile(Long caseId, FileUploadRequest request);
    void deleteFile(Long fileId);
    List<CaseFile> getFilesByCaseId(Long caseId);
    CaseFile getFileById(Long fileId);
}