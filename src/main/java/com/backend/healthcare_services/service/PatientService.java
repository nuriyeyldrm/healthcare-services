package com.backend.healthcare_services.service;

import com.backend.healthcare_services.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
}
