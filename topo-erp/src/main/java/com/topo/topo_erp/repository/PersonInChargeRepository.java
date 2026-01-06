package com.topo.topo_erp.repository;

import com.topo.topo_erp.model.PersonInCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PersonInChargeRepository extends JpaRepository<PersonInCharge, Long> {
    Optional<PersonInCharge> findByRut(String rut);
    Optional<PersonInCharge> findByEmail(String email);
    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
}