package com.backend.healthcare_services.service;

import com.backend.healthcare_services.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
}
