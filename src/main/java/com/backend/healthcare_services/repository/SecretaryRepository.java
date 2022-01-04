package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Department;
import com.backend.healthcare_services.domain.Secretary;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.dto.SecretaryDTO;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface SecretaryRepository extends JpaRepository<Secretary, Long> {

    Optional<SecretaryDTO> findByUserIdOrderByUserId(User userId) throws ResourceNotFoundException;

    Optional<Secretary> findByUserId(User userId) throws ResourceNotFoundException;

    Optional<SecretaryDTO> findByIdOrderById(Long id);

    List<SecretaryDTO> findByDepartment(Department department);

    List<SecretaryDTO> findAllBy();
}
