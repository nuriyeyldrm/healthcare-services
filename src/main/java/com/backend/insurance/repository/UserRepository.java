package com.backend.insurance.repository;

import com.backend.insurance.domain.User;
import com.backend.insurance.exception.ConflictException;
import com.backend.insurance.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email) throws ConflictException;

    Optional<User> findByEmail(String email) throws ResourceNotFoundException;

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE WHERE u.email =?1")
    int enableAppUser(String email);
}
