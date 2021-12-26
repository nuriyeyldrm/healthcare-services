package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Nurse;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.dto.NurseDTO;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {

    Optional<NurseDTO> findByUserIdOrderByUserId(User userId) throws ResourceNotFoundException;

    Optional<Nurse> findByUserId(User userId) throws ResourceNotFoundException;

    Optional<NurseDTO> findByIdOrderById(Long id) throws ResourceNotFoundException;

    List<NurseDTO> findAllBy();
}
