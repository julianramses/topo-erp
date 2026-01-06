package com.topo.topo_erp.service;

import com.topo.topo_erp.dto.PersonInChargeRequest;
import com.topo.topo_erp.dto.PersonInChargeResponse;
import java.util.List;

public interface PersonInChargeService {
    PersonInChargeResponse createPersonInCharge(PersonInChargeRequest request);
    PersonInChargeResponse getPersonInChargeById(Long id);
    PersonInChargeResponse getPersonInChargeByRut(String rut);
    List<PersonInChargeResponse> getAllPersonsInCharge();
    PersonInChargeResponse updatePersonInCharge(Long id, PersonInChargeRequest request);
    void deletePersonInCharge(Long id);
    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
}