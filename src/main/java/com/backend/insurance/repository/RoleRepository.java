package com.backend.insurance.repository;

import com.backend.insurance.domain.Role;
import com.backend.insurance.domain.enumeration.UserRole;
import com.backend.insurance.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(UserRole userRole) throws ResourceNotFoundException;
}
