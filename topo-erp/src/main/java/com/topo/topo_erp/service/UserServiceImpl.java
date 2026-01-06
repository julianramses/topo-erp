package com.topo.topo_erp.service;

import com.topo.topo_erp.model.User;
import com.topo.topo_erp.repository.UserRepository;
import com.topo.topo_erp.dto.UserRequest;
import com.topo.topo_erp.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .password(userRequest.getPassword()) // In real app, hash this!
                .role(userRequest.getRole())
                .licenseNumber(userRequest.getLicenseNumber())
                .specialization(userRequest.getSpecialization())
                .licenseExpiryDate(userRequest.getLicenseExpiryDate())
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setRole(userRequest.getRole());
        user.setLicenseNumber(userRequest.getLicenseNumber());
        user.setSpecialization(userRequest.getSpecialization());
        user.setLicenseExpiryDate(userRequest.getLicenseExpiryDate());

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return mapToResponse(user);
    }

    @Override
    public List<UserResponse> getUsersByRole(User.UserRole role) {
        return userRepository.findByRole(role)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .licenseNumber(user.getLicenseNumber())
                .specialization(user.getSpecialization())
                .licenseExpiryDate(user.getLicenseExpiryDate())
                .build();
    }
}