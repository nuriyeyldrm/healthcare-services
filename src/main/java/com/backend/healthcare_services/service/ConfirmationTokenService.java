package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.ConfirmationToken;
import com.backend.healthcare_services.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedDate(String token) {
        return confirmationTokenRepository.updateConfirmedDate(token, LocalDateTime.now());
    }
}
