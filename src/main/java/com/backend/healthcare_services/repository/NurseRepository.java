package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Nurse;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.dto.NurseDTO;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {

    @Query("SELECT new com.backend.healthcare_services.dto.NurseDTO(n) FROM Nurse n " +
            "WHERE n.id = ?1 and n.userId = ?2")
    Optional<NurseDTO> findByIdAndUserIdx(Long id, User userId) throws ResourceNotFoundException;

    Optional<NurseDTO> findByIdOrderById(Long id);

    @Query("SELECT new com.backend.healthcare_services.dto.NurseDTO(n) FROM Nurse n " +
            "WHERE n.userId = ?1")
    List<NurseDTO> findByUserIdx(User userId);

    @Query("SELECT new com.backend.healthcare_services.dto.NurseDTO(n) FROM Nurse n")
    List<NurseDTO> findAllByx();
}
