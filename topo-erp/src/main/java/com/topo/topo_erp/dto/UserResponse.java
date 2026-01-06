package com.topo.topo_erp.dto;

import com.topo.topo_erp.model.User;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private User.UserRole role;
    private String licenseNumber;
    private String specialization;
    private LocalDate licenseExpiryDate;
}