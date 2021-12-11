package com.backend.insurance.repository;

import com.backend.insurance.domain.User;
import com.backend.insurance.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email) throws ResourceNotFoundException;
}
