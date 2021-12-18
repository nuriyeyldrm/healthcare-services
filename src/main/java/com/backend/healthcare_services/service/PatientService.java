package com.backend.healthcare_services.service;

import com.backend.healthcare_services.domain.Patient;
import com.backend.healthcare_services.domain.User;
import com.backend.healthcare_services.dto.PatientDTO;
import com.backend.healthcare_services.exception.ResourceNotFoundException;
import com.backend.healthcare_services.repository.PatientRepository;
import com.backend.healthcare_services.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";
    private final static String PATIENT_NOT_FOUND_MSG = "patient with id %d not found";

    public PatientDTO findById(Long id, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        return patientRepository.findByIdAndUserIdx(id, user).orElseThrow(() ->
                new ResourceNotFoundException(String.format(PATIENT_NOT_FOUND_MSG, id)));
    }

    public List<PatientDTO> findAll() {
        return patientRepository.findAllByx();
    }

    public void addPatient(Long userId, Patient patient) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));

        patient.setUserId(user);
        patientRepository.save(patient);
    }
}
