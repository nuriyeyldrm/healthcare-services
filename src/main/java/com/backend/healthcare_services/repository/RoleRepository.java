package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Role;
import com.backend.healthcare_services.domain.enumeration.UserRole;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(UserRole userRole) throws ResourceNotFoundException;
}
