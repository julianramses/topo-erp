package com.topo.topo_erp.service;

import com.topo.topo_erp.dto.PersonInChargeRequest;
import com.topo.topo_erp.dto.PersonInChargeResponse;
import com.topo.topo_erp.model.PersonInCharge;
import com.topo.topo_erp.repository.PersonInChargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonInChargeServiceImpl implements PersonInChargeService {

    private final PersonInChargeRepository personInChargeRepository;

    @Override
    public PersonInChargeResponse createPersonInCharge(PersonInChargeRequest request) {
        // Validate RUT (8 digits)
        if (request.getRut() == null || !request.getRut().matches("\\d{8}")) {
            throw new IllegalArgumentException("RUT must be exactly 8 digits");
        }

        // Check if RUT already exists
        if (personInChargeRepository.existsByRut(request.getRut())) {
            throw new IllegalArgumentException("Person with RUT " + request.getRut() + " already exists");
        }

        // Check if email already exists
        if (request.getEmail() != null && personInChargeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email " + request.getEmail() + " already exists");
        }

        PersonInCharge person = PersonInCharge.builder()
                .name(request.getName())
                .rut(request.getRut())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .position(request.getPosition())
                .department(request.getDepartment())
                .build();

        PersonInCharge savedPerson = personInChargeRepository.save(person);
        return mapToResponse(savedPerson);
    }

    @Override
    public PersonInChargeResponse getPersonInChargeById(Long id) {
        PersonInCharge person = personInChargeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person in charge not found with id: " + id));
        return mapToResponse(person);
    }

    @Override
    public PersonInChargeResponse getPersonInChargeByRut(String rut) {
        PersonInCharge person = personInChargeRepository.findByRut(rut)
                .orElseThrow(() -> new RuntimeException("Person in charge not found with RUT: " + rut));
        return mapToResponse(person);
    }

    @Override
    public List<PersonInChargeResponse> getAllPersonsInCharge() {
        return personInChargeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PersonInChargeResponse updatePersonInCharge(Long id, PersonInChargeRequest request) {
        PersonInCharge person = personInChargeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person in charge not found with id: " + id));

        // Update fields if provided
        if (request.getName() != null) {
            person.setName(request.getName());
        }

        if (request.getEmail() != null) {
            // Check if email is being changed and if it already exists
            if (!request.getEmail().equals(person.getEmail()) &&
                    personInChargeRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email " + request.getEmail() + " already exists");
            }
            person.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null) {
            person.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getPosition() != null) {
            person.setPosition(request.getPosition());
        }

        if (request.getDepartment() != null) {
            person.setDepartment(request.getDepartment());
        }

        PersonInCharge updatedPerson = personInChargeRepository.save(person);
        return mapToResponse(updatedPerson);
    }

    @Override
    public void deletePersonInCharge(Long id) {
        PersonInCharge person = personInChargeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person in charge not found with id: " + id));

        // Check if person has assigned cases
        if (person.getCases() != null && !person.getCases().isEmpty()) {
            throw new RuntimeException("Cannot delete person in charge with assigned cases. Reassign cases first.");
        }

        personInChargeRepository.delete(person);
    }

    @Override
    public boolean existsByRut(String rut) {
        return personInChargeRepository.existsByRut(rut);
    }

    @Override
    public boolean existsByEmail(String email) {
        return personInChargeRepository.existsByEmail(email);
    }

    private PersonInChargeResponse mapToResponse(PersonInCharge person) {
        return PersonInChargeResponse.builder()
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