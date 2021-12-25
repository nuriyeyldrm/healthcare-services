package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Department;
import com.backend.healthcare_services.domain.enumeration.Departments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByName(Departments departments);
}
