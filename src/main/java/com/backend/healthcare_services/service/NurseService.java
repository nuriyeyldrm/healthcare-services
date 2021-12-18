package com.backend.healthcare_services.service;

import com.backend.healthcare_services.repository.NurseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NurseService {

    private final NurseRepository nurseRepository;
}
