package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Department;
import com.backend.healthcare_services.domain.Doctor;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.dto.DoctorDTO;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<DoctorDTO> findByUserIdOrderByUserId(User userId) throws ResourceNotFoundException;

    Optional<Doctor> findByUserId(User userId) throws ResourceNotFoundException;

    Optional<DoctorDTO> findByIdOrderById(Long id) throws ResourceNotFoundException;

    List<DoctorDTO> findByDepartment(Department department);

    List<DoctorDTO> findAllBy();
}
