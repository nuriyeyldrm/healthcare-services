package com.backend.healthcare_services.dto;

import com.backend.healthcare_services.domain.Department;
import com.backend.healthcare_services.domain.Nurse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NurseDTO {

    private Long id;
    private UserDTO userId;
    private Department department;

    public NurseDTO(Nurse nurse) {
        this.id = nurse.getId();
        this.userId = new UserDTO(nurse.getUserId());
        this.department = nurse.getDepartment();
    }
}
