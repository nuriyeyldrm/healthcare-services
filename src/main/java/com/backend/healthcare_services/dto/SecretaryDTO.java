package com.backend.healthcare_services.dto;

import com.backend.healthcare_services.domain.Secretary;
import com.backend.healthcare_services.domain.enumeration.Departments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SecretaryDTO {

    private Long id;
    private UserDTO userId;
    private Departments department;

    public SecretaryDTO(Secretary secretary) {
        this.id = secretary.getId();
        this.userId = new UserDTO(secretary.getUserId());
        this.department = secretary.getDepartment().getName();
    }
}
