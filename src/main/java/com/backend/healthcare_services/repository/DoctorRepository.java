package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
