package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.Department;
import com.backend.healthcare_services.domain.enumeration.Departments;
import com.backend.healthcare_services.dto.DoctorDTO;
import com.backend.healthcare_services.repository.DepartmentRepository;
import com.backend.healthcare_services.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DepartmentService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public List<DoctorDTO> findByDepartmentId(Departments departmentId) {
        Department department = departmentRepository.findByName(departmentId)
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        return doctorRepository.findByDepartment(department);
    }

}
