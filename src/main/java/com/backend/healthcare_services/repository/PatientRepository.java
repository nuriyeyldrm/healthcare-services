package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Patient;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.dto.PatientDTO;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<PatientDTO> findByIdAndUserId(Long id, User userId) throws ResourceNotFoundException;

    Optional<Patient> findByIdAndUserIdOrderById(Long id, User userId) throws ResourceNotFoundException;

    List<PatientDTO> findByUserIdOrderById(User userId) throws ResourceNotFoundException;

    Optional<PatientDTO> findByIdOrderById(Long id) throws ResourceNotFoundException;

    List<PatientDTO> findAllBy();
}
