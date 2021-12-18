package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.Patient;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import com.backend.healthcare_services.repository.PatientRepository;
import com.backend.healthcare_services.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final static String USER_NOT_FOUND_MSG = "user with id %s not found";

    public void addPatient(Long userId, Patient patient) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        patient.setUserId(user);
        patientRepository.save(patient);
    }
}
