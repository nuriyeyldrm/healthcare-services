package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.Department;
import com.backend.healthcare_services.domain.enumeration.Departments;
import com.backend.healthcare_services.dto.DoctorDTO;
import com.backend.healthcare_services.dto.NurseDTO;
import com.backend.healthcare_services.dto.SecretaryDTO;
import com.backend.healthcare_services.repository.DepartmentRepository;
import com.backend.healthcare_services.repository.DoctorRepository;
import com.backend.healthcare_services.repository.NurseRepository;
import com.backend.healthcare_services.repository.SecretaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final SecretaryRepository secretaryRepository;

    public List<DoctorDTO> findDoctorsByDepartmentId(Departments departmentId) {
        Department department = departmentRepository.findByName(departmentId)
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        return doctorRepository.findByDepartment(department);
    }

    public List<NurseDTO> findNursesByDepartmentId(Departments departmentId) {
        Department department = departmentRepository.findByName(departmentId)
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        return nurseRepository.findByDepartment(department);
    }

    public List<SecretaryDTO> findSecretariesByDepartmentId(Departments departmentId) {
        Department department = departmentRepository.findByName(departmentId)
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        return secretaryRepository.findByDepartment(department);
    }

}
