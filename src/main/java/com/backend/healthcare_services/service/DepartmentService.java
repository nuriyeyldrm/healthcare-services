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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final SecretaryRepository secretaryRepository;

    public List<DoctorDTO> findDoctorsByDepartmentName(Departments departmentName) {
        Department department = departmentRepository.findByName(departmentName)
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        return doctorRepository.findByDepartment(department);
    }

    public List<NurseDTO> findNursesByDepartmentName(Departments departmentName) {
        Department department = departmentRepository.findByName(departmentName)
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        return nurseRepository.findByDepartment(department);
    }

    public List<SecretaryDTO> findSecretariesByDepartmentName(Departments departmentName) {
        Department department = departmentRepository.findByName(departmentName)
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        return secretaryRepository.findByDepartment(department);
    }

    public Map<String, Object> findAllDepartmentStaff(Departments departmentName) {
        Department department = departmentRepository.findByName(departmentName)
                .orElseThrow(() -> new RuntimeException("Error: Department is not found."));

        List<DoctorDTO> doctors = doctorRepository.findByDepartment(department);

        List<NurseDTO> nurses = nurseRepository.findByDepartment(department);

        List<SecretaryDTO> secretaries = secretaryRepository.findByDepartment(department);

        Map<String, Object> map = new HashMap<>();

        map.put("Doctors", doctors);
        map.put("Secretaries", secretaries);
        map.put("Nurses", nurses);

        return map;
    }

}
