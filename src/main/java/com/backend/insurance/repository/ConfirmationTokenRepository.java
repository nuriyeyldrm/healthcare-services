package com.backend.insurance.repository;

import com.backend.insurance.domain.ConfirmationToken;
import com.backend.insurance.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token) throws ResourceNotFoundException;

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c SET c.confirmedDate = ?2 WHERE c.token = ?1")
    int updateConfirmedDate(String token, LocalDateTime confirmedDate);
}
