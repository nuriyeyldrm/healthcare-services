package com.backend.healthcare_services.repository;

import com.backend.healthcare_services.domain.Secretary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface SecretaryRepository extends JpaRepository<Secretary, Long> {
}
