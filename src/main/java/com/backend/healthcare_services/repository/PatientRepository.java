package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Patient;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.dto.PatientDTO;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT new com.backend.healthcare_services.dto.PatientDTO(p) FROM Patient p " +
            "WHERE p.id = ?1 and p.userId = ?2")
    Optional<PatientDTO> findByIdAndUserIdx(Long id, User userId) throws ResourceNotFoundException;

    @Query("SELECT new com.backend.healthcare_services.dto.PatientDTO(p) FROM Patient p " +
            "WHERE p.userId = ?1")
    List<PatientDTO> findByUserIdx(User userId);

    Optional<PatientDTO> findByIdOrderById(Long id);

    @Query("SELECT new com.backend.healthcare_services.dto.PatientDTO(p) FROM Patient p")
    List<PatientDTO> findAllByx();
}
