package com.topo.topo_erp.service;

import com.topo.topo_erp.model.User;
import com.topo.topo_erp.dto.UserRequest;
import com.topo.topo_erp.dto.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UserRequest userRequest);
    void deleteUser(Long id);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getUsersByRole(User.UserRole role);
}